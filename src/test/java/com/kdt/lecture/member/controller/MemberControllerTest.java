package com.kdt.lecture.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kdt.lecture.domain.order.MemberRepository;
import com.kdt.lecture.member.dto.MemberDto;
import com.kdt.lecture.member.service.MemberService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@AutoConfigureRestDocs
@SpringBootTest
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @AfterEach
    void tearDown() {
        memberRepository.deleteAll();
    }

    @Test
    void saveMember() throws Exception {
        // Given
        MemberDto request = MemberDto.builder()
                .name("김대휘")
                .nickName("maroo")
                .age(23)
                .address("Suwon")
                .description("-")
                .build();
        String requestString = objectMapper.writeValueAsString(request);

        // When // Then
        this.mockMvc.perform(post("/members")
                        .content(requestString)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("member-save",
                        requestFields(
                                fieldWithPath("id").type(JsonFieldType.VARIES).description("아이디"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("이름"),
                                fieldWithPath("nickName").type(JsonFieldType.STRING).description("별명"),
                                fieldWithPath("age").type(JsonFieldType.NUMBER).description("나이"),
                                fieldWithPath("address").type(JsonFieldType.STRING).description("주소"),
                                fieldWithPath("description").type(JsonFieldType.STRING).description("설명")
                        ),
                        responseFields(
                                fieldWithPath("statusCode").type(JsonFieldType.NUMBER).description("상태코드"),
                                fieldWithPath("data").type(JsonFieldType.NUMBER).description("데이터"),
                                fieldWithPath("serverDatetime").type(JsonFieldType.STRING).description("응답시간")
                        )
                ));
    }

    @Test
    void updateMember() throws Exception {
        // Given
        Long memberId = memberService.saveMember(
                MemberDto.builder()
                        .name("김대휘")
                        .nickName("maroo")
                        .age(23)
                        .address("suwon")
                        .description("--1")
                        .build()
        );

        MemberDto request = MemberDto.builder()
                .name("대휘김")
                .nickName("marooo")
                .age(43)
                .address("seoul")
                .description("--2")
                .build();
        String requestString = objectMapper.writeValueAsString(request);

        // When // Then
        this.mockMvc.perform(post("/members/{id}", memberId)
                        .content(requestString)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("member-update",
                        requestFields(
                                fieldWithPath("id").type(JsonFieldType.VARIES).description("아이디"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("이름"),
                                fieldWithPath("nickName").type(JsonFieldType.STRING).description("별명"),
                                fieldWithPath("age").type(JsonFieldType.NUMBER).description("나이"),
                                fieldWithPath("address").type(JsonFieldType.STRING).description("주소"),
                                fieldWithPath("description").type(JsonFieldType.STRING).description("설명")
                        ),
                        responseFields(
                                fieldWithPath("statusCode").type(JsonFieldType.NUMBER).description("상태코드"),
                                fieldWithPath("data").type(JsonFieldType.NUMBER).description("데이터"),
                                fieldWithPath("serverDatetime").type(JsonFieldType.STRING).description("응답시간")
                        )
                ));
    }

    @Test
    void getMember() throws Exception {
        // Given
        Long memberId = memberService.saveMember(
                MemberDto.builder()
                        .name("김대휘")
                        .nickName("marooo")
                        .age(22)
                        .address("suwon")
                        .description("--")
                        .build()
        );

        // When // Then
        this.mockMvc.perform(get("/members/{id}", memberId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("member-get",
                        responseFields(
                                fieldWithPath("statusCode").type(JsonFieldType.NUMBER).description("상태코드"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("데이터"),
                                fieldWithPath("serverDatetime").type(JsonFieldType.STRING).description("응답시간"),
                                fieldWithPath("data.id").type(JsonFieldType.VARIES).description("회원아이디"),
                                fieldWithPath("data.name").type(JsonFieldType.STRING).description("회원이름"),
                                fieldWithPath("data.nickName").type(JsonFieldType.STRING).description("회원별명"),
                                fieldWithPath("data.age").type(JsonFieldType.NUMBER).description("나이"),
                                fieldWithPath("data.address").type(JsonFieldType.STRING).description("주소"),
                                fieldWithPath("data.description").type(JsonFieldType.STRING).description("설명")
                        )
                ));
    }

}
