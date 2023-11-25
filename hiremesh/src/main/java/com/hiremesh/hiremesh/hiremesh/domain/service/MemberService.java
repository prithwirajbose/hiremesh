package com.hiremesh.hiremesh.hiremesh.domain.service;

import com.hiremesh.hiremesh.hiremesh.domain.entity.Member;
import com.hiremesh.hiremesh.hiremesh.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberService {

    final MemberRepository memberRepository;

    public Member findMember(String username) {
        return memberRepository.findByUsername(username);
    }

    public void update(Member member) {
        memberRepository.save(member);
    }

    public void resetToken(String token) {
        memberRepository.findByToken(token).ifPresent(member -> {
            member.reset(null);
            memberRepository.save(member);
        });
    }

}
