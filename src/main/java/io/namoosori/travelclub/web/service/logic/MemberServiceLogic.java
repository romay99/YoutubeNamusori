package io.namoosori.travelclub.web.service.logic;

import io.namoosori.travelclub.web.aggregate.club.CommunityMember;
import io.namoosori.travelclub.web.service.MemberService;
import io.namoosori.travelclub.web.service.sdo.MemberCdo;
import io.namoosori.travelclub.web.shared.NameValueList;
import io.namoosori.travelclub.web.store.MemberStore;
import io.namoosori.travelclub.web.util.exception.MemberDuplicationException;
import io.namoosori.travelclub.web.util.exception.NoSuchClubException;
import io.namoosori.travelclub.web.util.exception.NoSuchMemberException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberServiceLogic implements MemberService {

	@Autowired
	private MemberStore memberStore;


	@Override
	public String registerMember(MemberCdo member) {
		String email = member.getEmail();
		CommunityMember foundedMember = memberStore.retrieveByEmail(email);

		if (foundedMember != null) {
			throw new MemberDuplicationException("Member already exists with email");
		}
		foundedMember = new CommunityMember(
				member.getEmail() ,member.getName(),member.getPhoneNumber());
		foundedMember.setNickName(member.getNickName());
		foundedMember.setBirthDay(member.getBirthDay());
		foundedMember.checkValidation();
		memberStore.create(foundedMember);
		return foundedMember.getId();
	}

	@Override
	public CommunityMember findMemberById(String memberId) {
		return memberStore.retrieve(memberId);
	}

	@Override
	public CommunityMember findMemberByEmail(String memberEmail) {
		return memberStore.retrieveByEmail(memberEmail);
	}

	@Override
	public List<CommunityMember> findMembersByName(String name) {
		return memberStore.retrieveByName(name);
	}

	@Override
	public void modifyMember(String memberId, NameValueList nameValueList) {
		CommunityMember targetMember = memberStore.retrieve(memberId);
		if (targetMember == null) {
			throw new NoSuchMemberException("No such Member");
		}
		targetMember.modifyValues(nameValueList);

		memberStore.update(targetMember);
	}

	@Override
	public void removeMember(String memberId) {
		if (!memberStore.exists(memberId)) {
			throw new NoSuchMemberException("No Such Member");
		}
		memberStore.delete(memberId);
	}
}
