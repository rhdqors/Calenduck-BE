package com.example.calenduck.domain.user.controller;

import com.example.calenduck.domain.user.entity.User;
import com.example.calenduck.domain.user.service.KakaoUserService;
import com.example.calenduck.global.jwt.JwtUtil;
import com.example.calenduck.global.message.ResponseMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
public class KakaoUserController {

    private final KakaoUserService kakaoService;
    private final JwtUtil jwtUtil;

    @Operation(summary = "카카오 로그인", description = "카카오 로그인")
    @GetMapping("/users/kakao/login")
    public synchronized ResponseEntity<?> kakaoLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
        // code: 카카오 서버로부터 받은 인가 코드
        User kakaoUser = kakaoService.kakaoLogin(code, response);
        String createToken =  jwtUtil.createToken(kakaoUser.getNickName(), kakaoUser.getKakaoEmail(), kakaoUser.getRole());

        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, createToken);

        return ResponseMessage.SuccessResponse("로그인 성공", "");
    }

}

//      위의 코드를 통해 받아온 공연코드값을 GET 방식으로 자바에서 request를 보내고 xml을 읽어오는 방법
//    public static void main(String[] args) {
//        try {
//            // Create URL object with the API endpoint
//            URL url = new URL("http://kopis.or.kr/openApi/restful/pblprfr/PF134308?service=60a3d3573c5e4d8bb052a4abebff27b6");
//
//            // Open a connection to the URL
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//
//            // Set the request method to GET
//            connection.setRequestMethod("GET");
//
//            // Get the response code
//            int responseCode = connection.getResponseCode();
//            System.out.println("Response Code: " + responseCode);
//
//            // Read the response
//            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//            String line;
//            StringBuilder response = new StringBuilder();
//            while ((line = reader.readLine()) != null) {
//                response.append(line);
//            }
//            reader.close();
//
//            // Print the XML response
//            System.out.println("Response XML:\n" + response.toString());
//
//            // Close the connection
//            connection.disconnect();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
