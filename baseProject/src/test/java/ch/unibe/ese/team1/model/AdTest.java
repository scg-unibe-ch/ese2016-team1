package ch.unibe.ese.team1.model;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import ch.unibe.ese.team1.model.Ad;
import ch.unibe.ese.team1.model.AdPicture;
import ch.unibe.ese.team1.model.Visit;

public class AdTest {
	
	private Ad ad = new Ad();
	private SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd");

	@Test
	public void testAuctionEndTimeBeforeToday() throws ParseException {
		Date d = ft.parse("2015-01-01");
		ad.setEndOfAuction(d);
		assertTrue(ad.getAuctionEndTimeBeforeToday());
	}
	
	@Test
	public void testGetAuctionEndedCurrentBiddingHigherThanRetailPrice() {
		ad.setRetailPrice(500);
		ad.setCurrentBidding(501);
		assertTrue(ad.getAuctionEndedCurrentBiddingHigherThanRetailPrice());
	}
	
	@Test
	public void testAuctionEndedTimeBeforeToday() throws ParseException {
		Date d = ft.parse("2015-01-01");
		ad.setEndOfAuction(d);
		ad.setCurrentBidding(501);
		ad.setCurrentBidding(500);
		assertTrue(ad.getAuctionEnded());
	}
	
	@Test
	public void testAuctionEndedCurrentBidding() throws ParseException {
		Date d = ft.parse("2032-01-01");
		ad.setEndOfAuction(d);
		ad.setRetailPrice(500);
		ad.setCurrentBidding(501);
		assertTrue(ad.getAuctionEnded());
	}
	
	@Test
	public void testAuctionNotEnded() throws ParseException {
		Date d = ft.parse("2032-01-01");
		ad.setEndOfAuction(d);
		ad.setRetailPrice(501);
		ad.setCurrentBidding(500);
		assertFalse(ad.getAuctionEnded());
	}
	
	@Test
	public void testAddFifteenMinutesToAuctionEnded() {
		Date today = new Date();
		final int fifteenMinutesInMilliseconds = 900000;
		long todayPlusFifteen = today.getTime() + fifteenMinutesInMilliseconds + 2;
		long d = today.getTime() + 1000;
		ad.setEndOfAuction(new Date(d));
		ad.addFifteenMinutesToAuctionEndedIfNecessary();
		assertEquals( new Date(todayPlusFifteen).getTime() - ad.getEndOfAuction().getTime() < 100, true);
		
	}
	
}
