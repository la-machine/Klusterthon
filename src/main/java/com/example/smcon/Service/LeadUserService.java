package com.example.smcon.Service;


import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.smcon.DTO.AuthRequest;
import com.example.smcon.DTO.UserDto;
import com.example.smcon.Model.LeadUsers;
import com.example.smcon.Model.Role;
import com.example.smcon.Repository.LeadUserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LeadUserService {
    @Autowired
    private LeadUserRepository userRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private EmailSender emailSender;

    public ResponseEntity<?> createUser(UserDto user){
        LeadUsers userExist = userRepository.findByEmail(user.getEmail());
        if(userExist != null){
            return ResponseEntity.status(400).body("Employee already exist");
        }
        String code = RandomStringUtils.randomAlphanumeric(6);;

        LeadUsers emp = LeadUsers.builder().fname(user.getFname()).lname(user.getLname()).tel(user.getTel()).email(user.getEmail())
            .password(encoder.encode(user.getPassword())).role(Role.User).verificationCode(code).build();
        userRepository.save(emp);
        emailSender.send(user.getEmail(), buildEmail(user.getFname(), code));
        
        return ResponseEntity.status(200).body("Employee saved successfully : " + emp);
    }

    public ResponseEntity<?> getAllUsers(){
        return ResponseEntity.status(200).body(userRepository.findAll());
    }

    public ResponseEntity<?> getByEmail(String email){
        LeadUsers emp = userRepository.findByEmail(email);
        if(emp == null){
            return ResponseEntity.status(400).body("There is no user with this email " + email);
        }

        return ResponseEntity.status(200).body(emp);
    }

    public ResponseEntity<?> updateUser(UserDto user){
        LeadUsers userExist = userRepository.findByEmail(user.getEmail());
        if(userExist == null){
            return ResponseEntity.status(400).body("There is no user with this email " + user.getEmail());
        }
        userExist.setTel(user.getTel());
        userExist.setFname(user.getFname());
        userExist.setLname(user.getLname());
        userExist.setEmail(user.getEmail());
        userRepository.save(userExist);
        return ResponseEntity.status(200).body("user updated");
    }

    public String authenticate(AuthRequest request){
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        LeadUsers emp = userRepository.findByEmail(request.getEmail());

        String jwtToken = jwtService.generateToken(emp);
        return jwtToken;
    }

    public ResponseEntity<?> verifyUser(String email, String code){
        LeadUsers tempUser = userRepository.findByEmail(email);
        System.out.println(tempUser.getEmail()+ " " + tempUser.getVerificationCode() + " compare " + code);
        if (tempUser != null && tempUser.getVerificationCode().equals(code)) {
            tempUser.setEnable(true);
            userRepository.save(tempUser);
            return ResponseEntity.status(200).body("user Verified ");
        }else{
            return ResponseEntity.badRequest().body("Error unable to verify user");
        }
    
    }

    public String buildEmail(String name, String link) {
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
                "\n" +
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                "\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
                "        \n" +
                "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "          <tbody><tr>\n" +
                "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
                "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td style=\"padding-left:10px\">\n" +
                "                  \n" +
                "                    </td>\n" +
                "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Confirm your email</span>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "              </a>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "        </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                "      <td>\n" +
                "        \n" +
                "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "\n" +
                "\n" +
                "\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                "        \n" +
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Thank you for registering. Please enter this code below to activate your account: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <b> " + link + "</b> </p></blockquote>\n Link will expire in 15 minutes. <p>See you soon</p>" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                "\n" +
                "</div></div>";
        }

}
