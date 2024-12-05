package com.example.devjobs.user.service.implement;

import com.example.devjobs.user.common.CertificationNumber;
import com.example.devjobs.user.dto.request.auth.*;
import com.example.devjobs.user.dto.response.ResponseDto;
import com.example.devjobs.user.dto.response.auth.*;
import com.example.devjobs.user.entity.Certification;
import com.example.devjobs.user.entity.User;
import com.example.devjobs.user.provider.EmailProvider;
import com.example.devjobs.user.provider.JwtProvider;
import com.example.devjobs.user.repository.CertificationRepository;
import com.example.devjobs.user.repository.UserRepository;
import com.example.devjobs.user.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class AuthServiceImplement implements AuthService {

    private final UserRepository userRepository;

    private final CertificationRepository certificationRepository;

    private final JwtProvider jwtProvider;
    private final EmailProvider emailProvider;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public ResponseEntity<? super IdCheckResponseDto> idCheck(IdCheckRequestDto dto) {

        try {

            String userId = dto.getUserId();
            boolean isExistId = userRepository.existsByUserId(userId);

            if(isExistId) return IdCheckResponseDto.duplicatedId();

        } catch (Exception exception){
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        return IdCheckResponseDto.success();

    }

    @Override
    public ResponseEntity<? super NicknameCheckResponseDto> nicknameCheck(NicknameCheckRequestDto dto) {

        try {
            String userId = dto.getUserId();
            String nickname = dto.getNickname();

            boolean isExistId = userRepository.existsById(userId);
            if(isExistId) return NicknameCheckResponseDto.duplicateId();

            boolean isExistNickname = userRepository.existsByNickname(nickname);
            if(isExistNickname) return NicknameCheckResponseDto.duplicateNickname();

        } catch (Exception exception){
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }


        return NicknameCheckResponseDto.success();
    }

    @Override
    public ResponseEntity<? super EmailCertificationResponseDto> emailCertification(EmailCertificationRequestDto dto) {
        try {

            String userId = dto.getUserId();
            String nickname = dto.getNickname();
            String email = dto.getEmail();

            boolean isExistId = userRepository.existsById(userId);
            if(isExistId) return EmailCertificationResponseDto.duplicateId();

            boolean isExistNickname = userRepository.existsByNickname(nickname);
            if(isExistNickname) return EmailCertificationResponseDto.duplicateNickname();

            String certificationNumber = CertificationNumber.getCertificationNumber();

            boolean isSuccessed = emailProvider.sendCertificationMail(email, certificationNumber);
            if(!isSuccessed) return EmailCertificationResponseDto.mailSendFail();

            Certification certification = new Certification(userId, email, certificationNumber);
            certificationRepository.save(certification);

        } catch (Exception exception){
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        return EmailCertificationResponseDto.success();
    }

    @Override
    public ResponseEntity<? super CheckCertificationResponseDto> checkCertification(CheckCertificationRequestDto dto) {

        try {

            String userId = dto.getUserId();
            String email = dto.getEmail();
            String certificationNumber = dto.getCertificationNumber();

            Certification certification = certificationRepository.findByUserId(userId);
            if(certification == null) return CheckCertificationResponseDto.certificationFail();

            boolean isMatched = certification.getEmail().equals(email) && certification.getCertificationNumber().equals(certificationNumber);
            if(!isMatched) return CheckCertificationResponseDto.certificationFail();

        } catch (Exception exception){
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }
        return CheckCertificationResponseDto.success();
    }

    @Override
    public ResponseEntity<? super SignUpResponseDto> signUp(SignUpRequestDto dto) {
        try {

            String userId = dto.getUserId();
            String userCode = dto.getUserCode();
            String nickname = dto.getNickname();

            if(userCode != null && !userCode.isEmpty()){
                boolean isExistUserCode = userRepository.existsByUserCode(userCode);
                if(isExistUserCode) return SignUpResponseDto.duplicateId();

                String email = dto.getEmail();
                String type = dto.getType();
                User user = new User(userCode, nickname, email, type);
                userRepository.save(user);
            }else {
                boolean isExistId = userRepository.existsById(userId);
                if(isExistId) return SignUpResponseDto.duplicateId();

                boolean isExistNickname = userRepository.existsByNickname(nickname);
                if(isExistNickname) return SignUpResponseDto.duplicateNickname();

                String email = dto.getEmail();
                String certificationNumber = dto.getCertificationNumber();

                Certification certification = certificationRepository.findByUserId(userId);
                boolean isMatched = certification.getEmail().equals(email) &&
                        certification.getCertificationNumber().equals(certificationNumber);
                if(!isMatched) return SignUpResponseDto.certificationFail();

                String password = dto.getPassword();
                String encodedPassword = passwordEncoder.encode(password);
                dto.setPassword(encodedPassword);

                User user = new User(dto);
                userRepository.save(user);

                certificationRepository.deleteByUserId(userId);
            }

        } catch (Exception exception){
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        return SignUpResponseDto.success();
    }

    @Override
    public ResponseEntity<? super SignInResponseDto> signIn(SignInRequestDto dto) {
        String token = null;
        try {
            String userId = dto.getUserId();
            User user = userRepository.findByUserId(userId);
            if (user == null) {
                return SignInResponseDto.signInFail();  // 로그인 실패 처리
            }

            String password = dto.getPassword();
            String encodedPassword = user.getPassword();
            boolean isMatched = passwordEncoder.matches(password, encodedPassword);
            if (!isMatched) {
                return SignInResponseDto.signInFail();  // 비밀번호 불일치 처리
            }

            // JWT 토큰 생성
            token = jwtProvider.create(userId);

            // 성공적으로 로그인 시 사용자 정보와 토큰 포함하여 반환
            return ResponseEntity.ok(SignInResponseDto.success(token, user));

        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseDto.databaseError());
        }
    }

}
