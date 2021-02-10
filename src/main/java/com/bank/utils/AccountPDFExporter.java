package com.bank.utils;

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

import com.bank.model.dao.AccountBalance;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class AccountPDFExporter {
	private List<AccountBalance> list;

	public AccountPDFExporter(List<AccountBalance> list) {
		this.list = list;
	}

	private void writeTableHeader(PdfPTable table) {
		PdfPCell cell = new PdfPCell();
		cell.setBackgroundColor(Color.BLUE);
		cell.setPadding(5);

		Font font = FontFactory.getFont(FontFactory.HELVETICA);
		font.setColor(Color.WHITE);

		cell.setPhrase(new Phrase("ID", font));

		table.addCell(cell);

		cell.setPhrase(new Phrase("Credit", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Debit", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Balance", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Date", font));
		table.addCell(cell);
	}

	private void writeTableData(PdfPTable table) {
		for (AccountBalance record : list) {
			table.addCell(String.valueOf(record.getId()));
			table.addCell(String.valueOf(Objects.isNull(record.getCredit()) ? "0" : record.getCredit()));
			table.addCell(String.valueOf(Objects.isNull(record.getDebit()) ? "0" : record.getDebit()));
			table.addCell(String.valueOf(record.getBalance()));
			table.addCell(String.valueOf(record.getDate()));
		}
	}

	public byte[] export(String filename, String accountId, String fromDate, String toDate)
			throws DocumentException, IOException {
		Document document = new Document(PageSize.A4);

		PdfWriter.getInstance(document, new FileOutputStream(filename));

		document.open();
		Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		font.setSize(18);
		font.setColor(Color.BLUE);

		String header = new StringBuilder("Account Id::").append(accountId).append("; Transactions from::")
				.append(fromDate).append(" to::").append(toDate).toString();

		Paragraph p = new Paragraph(header, font);
		p.setAlignment(Paragraph.ALIGN_CENTER);

		document.add(p);

		PdfPTable table = new PdfPTable(5);
		table.setWidthPercentage(100f);
		table.setWidths(new float[] { 1.0f, 3.0f, 3.0f, 3.0f, 2.5f });
		table.setSpacingBefore(10);

		writeTableHeader(table);
		writeTableData(table);

		document.add(table);
		document.close();

		Path pdfPath = Paths.get(filename);
		byte[] pdf = Files.readAllBytes(pdfPath);
		return pdf;
	}
}