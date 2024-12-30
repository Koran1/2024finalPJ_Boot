package com.ict.finalpj.domain.user.service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;

	public void sendMail(String randomNumber, String toMail){
		try {
			MimeMessage mimeMessage = javaMailSender.createMimeMessage();

			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");

            // 메일을 받을 수신자 설정
            mimeMessageHelper.setTo(toMail);
            // 메일의 제목 설정
			mimeMessageHelper.setSubject("[Campers] 이메일 인증 메일입니다.");
			
			// 내용
			mimeMessageHelper.setText(
				    "<div style='width: 100%; max-width: 600px; color: #333;'>"
				    + "    <table style='width: 100%; border-collapse: collapse;'>"
				    + "        <thead>"
				    + "            <tr>"
				    + "                <th style='background-color: #02B08A; color: white; padding: 15px; text-align: center;'>"
				    + "                    <h2>Campers 이메일 인증</h2>"
				    + "                </th>"
				    + "            </tr>"
				    + "        </thead>"
				    + "        <tbody>"
				    + "            <tr>"
				    + "                <td style='padding: 20px; text-align: center;'>"
				    + "                    <p style='font-size: 16px;'>Campers 이용해 주셔서 감사합니다.</p>"
				    + "                    <p style='font-size: 16px;'>아래 인증 번호를 입력하여 인증을 완료해 주세요.</p>"
				    + "                    <p style='font-size: 18px; font-weight: bold; color: #02B08A;'>"
				    + "                        인증 번호: <span style='font-size: 20px;'>" + randomNumber + "</span>"
				    + "                    </p>"
				    + "                </td>"
				    + "            </tr>"
				    + "            <tr>"
				    + "                <td style='padding: 20px; text-align: center;'>"
				    + "                    <p style='font-size: 14px; color: #777;'>"
				    + "                        이 메일은 Campers 서비스 이용 중 발송된 메일입니다.<br>"
				    + "                        인증 번호를 요청하지 않으셨다면 이 메일을 무시하셔도 됩니다."
				    + "                    </p>"
				    + "                </td>"
				    + "            </tr>"
				    + "        </tbody>"
				    + "    </table>"
				    + "</div>"
				, true);
			mimeMessageHelper.setFrom("campers@mtl.com", "Campers 관리자");

            javaMailSender.send(mimeMessage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
