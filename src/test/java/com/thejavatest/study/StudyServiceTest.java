package com.thejavatest.study;

import com.thejavatest.domain.Member;
import com.thejavatest.member.MemberService;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudyServiceTest {

//    @Mock
//    MemberService memberService;
//    @Mock
//    StudyRepository studyRepository;

    @Test
    void createStudyService(@Mock MemberService memberService,
                            @Mock StudyRepository studyRepository) {

        StudyService studyService = new StudyService(memberService, studyRepository);

        Member member = new Member();
        member.setId(1L);
        member.setEmail("keesun@email.com");

        when(memberService.findById(any())).thenReturn(Optional.of(member));

        Optional<Member> findById = memberService.findById(1L);
        Assertions.assertEquals("keesun@email.com", findById.get().getEmail());

        when(memberService.findById(1L)).thenThrow();
        doThrow(new IllegalArgumentException()).when(memberService).validate(1L);
        memberService.validate(1L);
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            memberService.validate(1L);
        });
    }

}