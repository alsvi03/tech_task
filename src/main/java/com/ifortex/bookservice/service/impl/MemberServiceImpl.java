package com.ifortex.bookservice.service.impl;

import com.ifortex.bookservice.model.Book;
import com.ifortex.bookservice.model.Member;
import com.ifortex.bookservice.service.MemberService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MemberServiceImpl implements MemberService {

    private List<Member> members;

    @Override
    public Member findMember() {
        return members.stream()
                .filter(member -> member.getBorrowedBooks() != null && member.getBorrowedBooks().stream()
                        .anyMatch(book -> book.getGenres().contains("Romance")))
                .max(Comparator.comparing(member -> member.getBorrowedBooks().stream()
                        .filter(book -> book.getGenres().contains("Romance"))
                        .map(Book::getPublicationDate)
                        .min(LocalDateTime::compareTo).orElse(LocalDateTime.MAX)))
                .orElse(null);
    }

    @Override
    public List<Member> findMembers() {
        return members.stream()
                .filter(member -> member.getMembershipDate().getYear() == 2023 && (member.getBorrowedBooks() == null || member.getBorrowedBooks().isEmpty()))
                .collect(Collectors.toList());
    }
}
