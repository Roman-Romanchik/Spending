package org.aren_rend.model;

import javafx.collections.ObservableList;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class MainMenuModel {
	public String getAllSpending(ObservableList<String> notes) {
		return calculateSpending(notes);
	}

	public String getMonthSpending(ObservableList<String> notes) {

		LocalDate now = LocalDate.now();
		Pattern amountPattern = Pattern.compile(":\\s*(\\d+)\\s*rub");
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

		int amount = 0;
		boolean isCurrentMonth = false;

		for (String line : notes) {
			line = line.trim();

			if (line.startsWith("---") && line.endsWith("---")) {
				String dateStr = line.substring(3, line.length() - 3);
				try {
					LocalDate date = LocalDate.parse(dateStr, dateFormatter);
					isCurrentMonth = (date.getYear() == now.getYear() &&
							date.getMonthValue() == now.getMonthValue());
				} catch (Exception e) {
					isCurrentMonth = false;
				}
			}
			else if (isCurrentMonth && line.matches("\\d+\\.\\s+.*")) {
				var matcher = amountPattern.matcher(line);
				if (matcher.find()) {
					amount += Integer.parseInt(matcher.group(1));
				}
			}
		}

		return "Month: " + amount;
	}

	private String calculateSpending(List<String> notes) {
		Pattern pattern = Pattern.compile(":(\\s*)(\\d+)(\\s*)rub");
		int amount = 0;
		for(String note : notes) {
			Matcher matcher = pattern.matcher(note);
			if(matcher.find()) {
				amount += Integer.parseInt(matcher.group(2));
			}
		}
		return "All: " + amount;
	}

}
