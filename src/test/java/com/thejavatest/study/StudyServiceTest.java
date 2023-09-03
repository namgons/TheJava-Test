package com.thejavatest.study;

import com.thejavatest.domain.Member;
import com.thejavatest.domain.Study;
import com.thejavatest.member.MemberService;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudyServiceTest {

    @Mock
    MemberService memberService;
    @Mock
    StudyRepository studyRepository;

    @Test
    void createStudyService(@Mock MemberService memberService,
                            @Mock StudyRepository studyRepository) {

        StudyService studyService = new StudyService(memberService, studyRepository);

        Member member = new Member();
        member.setId(1L);
        member.setEmail("keesun@email.com");

        when(memberService.findById(any())).thenReturn(Optional.of(member));

        Optional<Member> findById = memberService.findById(1L);
        assertEquals("keesun@email.com", findById.get().getEmail());

        when(memberService.findById(1L)).thenThrow();
        doThrow(new IllegalArgumentException()).when(memberService).validate(1L);
        memberService.validate(1L);
        assertThrows(IllegalArgumentException.class, () -> {
            memberService.validate(1L);
        });
    }

    @Test
    void createStudyService() {
        StudyService studyService = new StudyService(memberService, studyRepository);
        assertNotNull(studyService);

        Member member = new Member();
        member.setId(1L);
        member.setEmail("keesun@email.com");

        Study study = new Study(10, "테스트");

        when(memberService.findById(1L)).thenReturn(Optional.of(member));
        when(studyRepository.save(study)).thenReturn(study);

        studyService.createNewStudy(1L, study);

        assertEquals(member.getId(), study.getOwnerId());

        verify(memberService, times(1)).notify(study);
        verify(memberService, never()).validate(any());
    }

}