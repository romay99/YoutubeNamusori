package io.namoosori.travelclub.web.service.logic;

import io.namoosori.travelclub.web.aggregate.club.TravelClub;
import io.namoosori.travelclub.web.service.ClubService;
import io.namoosori.travelclub.web.service.sdo.TravelClubCdo;
import io.namoosori.travelclub.web.shared.NameValueList;
import io.namoosori.travelclub.web.store.ClubStore;
import io.namoosori.travelclub.web.util.exception.NoSuchClubException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClubServiceLogic implements ClubService {

	@Autowired
	private ClubStore clubStore;

	@Override
	public String registerClub(TravelClubCdo club) {
		TravelClub newClub = new TravelClub(club.getName(), club.getIntro());
		newClub.checkValidation();
		return clubStore.create(newClub);
	}

	@Override
	public TravelClub findClubById(String id) {
		return clubStore.retrieve(id);
	}

	@Override
	public List<TravelClub> findClubsByName(String name) {
		return clubStore.retrieveByName(name);
	}

	@Override
	public List<TravelClub> findAll() {
		return null;
	}

	@Override
	public void modify(String clubId, NameValueList nameValues) {
		TravelClub foundedClub = clubStore.retrieve(clubId);
		if (foundedClub == null) {
			throw new NoSuchClubException("No Such Club with Id" + clubId);
		}
		foundedClub.modifyValues(nameValues);
		clubStore.update(foundedClub);

	}

	@Override
	public void remove(String clubId) {
		if (clubStore.exists(clubId)) {
			throw new NoSuchClubException("no such club");
		}
		clubStore.delete(clubId);

	}
}
