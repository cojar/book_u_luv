package com.project.bookuluv.controller;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import static org.mockito.ArgumentMatchers.any;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest
public class UserControllerTest {
//    @Autowired
//    MockMvc mockMvc;
//
//    @MockBean
//    MemberService userService;
//
//    @Autowired
//    ObjectMapper objectMapper;
//
//    @Test
//    @DisplayName("회원가입 성공")
//    void join() throws Exception {
//        String userName = "admin";
//        String password = "1q2w3e4r";
//        mockMvc.perform(post("/api/v1/users/join")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsBytes(new MemberJoinRequest(userName, password))))
//                .andDo(print())
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    @DisplayName("회원가입 실패 -> userName 중복")
//    void join_fail() throws Exception {
//        String userName = "admin";
//        String password = "1q2w3e4r";
//
//        when(userService.join(any(), any()))
//                .thenThrow(new RuntimeException("userId 중복"));
//
//        mockMvc.perform(post("/api/v1/users/join")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsBytes(new MemberJoinRequest(userName, password))))
//                .andDo(print())
//                .andExpect(status().isOk());
//    }
}
