package finance.progoti.service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import finance.progoti.repository.BillRepository;
import finance.progoti.user.model.Product;

@Service
public class ProductService {
	@Autowired
	private BillRepository repo;
	
	public List<Product> listAll(){
		return repo.findAll(Sort.by(Direction.DESC, "id"));
	}
	
	public List<Product> listAllProduct(){
		return repo.findAll();
	}
	
	public void save(Product product) {
		repo.save(product);
	}
	
	public List<Product> findAllById(Long id){
		return repo.findAllById(id);
	}
	
	public Product get(Long id) {
		return repo.findById(id).get();
	}
	
	public void delete(Long id) {
		repo.deleteById(id);
	}
	
	public List<Product> findBySubmittedby(String submittedBy) {
		return repo.findAllBySubmittedby(submittedBy);
	}
	
	public List<Product> findByApprovedby(String approvedBy) {
		return repo.findAllByApprovedby(approvedBy);
	}
	
	public List<Product> findByPaidby(String paidBy) {
		return repo.findAllByPaidby(paidBy);
	}

	public boolean createPdf(List<Product> listBills, ServletContext context, HttpServletRequest request,
			HttpServletResponse response) {
		
		Document document = new Document(PageSize.A4, 15, 15, 45, 30);
		try {
			String filePath = context.getRealPath("/resources/reports");
			File file = new File(filePath);
			boolean exists = new File(filePath).exists();
			if(!exists)
			{
				new File(filePath).mkdirs();
			}
			
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file+"/"+"listBills"+".pdf"));
			document.open();
			
			Font mainFont = FontFactory.getFont("Arial", 10, BaseColor.BLACK);
			
			Paragraph paragraph = new Paragraph("All Bills", mainFont);
			paragraph.setAlignment(Element.ALIGN_CENTER);
			paragraph.setIndentationLeft(50);
			paragraph.setIndentationRight(50);
			paragraph.setSpacingAfter(10);
			document.add(paragraph);
			
			PdfPTable table = new PdfPTable(9);
			table.setWidthPercentage(100);
			table.setSpacingBefore(10f);
			table.setSpacingAfter(10);
			
			Font tableHeader = FontFactory.getFont("Arial", 10, BaseColor.BLACK);
			Font tableBody = FontFactory.getFont("Arial", 9, BaseColor.BLACK);
			
			float[] columnWidths = {2f,2f,2f,2f};
			table.setWidths(columnWidths);
			
			PdfPCell date = new PdfPCell(new Paragraph("Date And Time", tableHeader));
			date.setBorderColor(BaseColor.BLACK);
			date.setPaddingLeft(10);
			date.setHorizontalAlignment(Element.ALIGN_CENTER);
			date.setVerticalAlignment(Element.ALIGN_CENTER);
			date.setBackgroundColor(BaseColor.GRAY);
			date.setExtraParagraphSpace(5f);
			table.addCell(date);
			
			PdfPCell purpose = new PdfPCell(new Paragraph("Purpose", tableHeader));
			purpose.setBorderColor(BaseColor.BLACK);
			purpose.setPaddingLeft(10);
			purpose.setHorizontalAlignment(Element.ALIGN_CENTER);
			purpose.setVerticalAlignment(Element.ALIGN_CENTER);
			purpose.setBackgroundColor(BaseColor.GRAY);
			purpose.setExtraParagraphSpace(5f);
			table.addCell(purpose);
			
			PdfPCell from = new PdfPCell(new Paragraph("From", tableHeader));
			from.setBorderColor(BaseColor.BLACK);
			from.setPaddingLeft(10);
			from.setHorizontalAlignment(Element.ALIGN_CENTER);
			from.setVerticalAlignment(Element.ALIGN_CENTER);
			from.setBackgroundColor(BaseColor.GRAY);
			from.setExtraParagraphSpace(5f);
			table.addCell(from);
			
			PdfPCell to = new PdfPCell(new Paragraph("To", tableHeader));
			to.setBorderColor(BaseColor.BLACK);
			to.setPaddingLeft(10);
			to.setHorizontalAlignment(Element.ALIGN_CENTER);
			to.setVerticalAlignment(Element.ALIGN_CENTER);
			to.setBackgroundColor(BaseColor.GRAY);
			to.setExtraParagraphSpace(5f);
			table.addCell(to);
			
			PdfPCell mode = new PdfPCell(new Paragraph("Mode", tableHeader));
			mode.setBorderColor(BaseColor.BLACK);
			mode.setPaddingLeft(10);
			mode.setHorizontalAlignment(Element.ALIGN_CENTER);
			mode.setVerticalAlignment(Element.ALIGN_CENTER);
			mode.setBackgroundColor(BaseColor.GRAY);
			mode.setExtraParagraphSpace(5f);
			table.addCell(mode);
			
			PdfPCell submittedBy = new PdfPCell(new Paragraph("Submitted By", tableHeader));
			submittedBy.setBorderColor(BaseColor.BLACK);
			submittedBy.setPaddingLeft(10);
			submittedBy.setHorizontalAlignment(Element.ALIGN_CENTER);
			submittedBy.setVerticalAlignment(Element.ALIGN_CENTER);
			submittedBy.setBackgroundColor(BaseColor.GRAY);
			submittedBy.setExtraParagraphSpace(5f);
			table.addCell(submittedBy);
			
			PdfPCell approvedBy = new PdfPCell(new Paragraph("Approved By", tableHeader));
			approvedBy.setBorderColor(BaseColor.BLACK);
			approvedBy.setPaddingLeft(10);
			approvedBy.setHorizontalAlignment(Element.ALIGN_CENTER);
			approvedBy.setVerticalAlignment(Element.ALIGN_CENTER);
			approvedBy.setBackgroundColor(BaseColor.GRAY);
			approvedBy.setExtraParagraphSpace(5f);
			table.addCell(approvedBy);
			
			PdfPCell paidBy = new PdfPCell(new Paragraph("Paid By", tableHeader));
			paidBy.setBorderColor(BaseColor.BLACK);
			paidBy.setPaddingLeft(10);
			paidBy.setHorizontalAlignment(Element.ALIGN_CENTER);
			paidBy.setVerticalAlignment(Element.ALIGN_CENTER);
			paidBy.setBackgroundColor(BaseColor.GRAY);
			paidBy.setExtraParagraphSpace(5f);
			table.addCell(paidBy);
			
			PdfPCell note = new PdfPCell(new Paragraph("Note", tableHeader));
			note.setBorderColor(BaseColor.BLACK);
			note.setPaddingLeft(10);
			note.setHorizontalAlignment(Element.ALIGN_CENTER);
			note.setVerticalAlignment(Element.ALIGN_CENTER);
			note.setBackgroundColor(BaseColor.GRAY);
			note.setExtraParagraphSpace(5f);
			table.addCell(note);
			
			for(Product product: listBills)
			{
				PdfPCell dateValue = new PdfPCell(new Paragraph(product.getDate(), tableBody));
				dateValue.setBorderColor(BaseColor.BLACK);
				dateValue.setPaddingLeft(10);
				dateValue.setHorizontalAlignment(Element.ALIGN_CENTER);
				dateValue.setVerticalAlignment(Element.ALIGN_CENTER);
				dateValue.setBackgroundColor(BaseColor.WHITE);
				dateValue.setExtraParagraphSpace(5f);
				table.addCell(dateValue);
				
				PdfPCell purposeValue = new PdfPCell(new Paragraph(product.getPurpose(), tableBody));
				purposeValue.setBorderColor(BaseColor.BLACK);
				purposeValue.setPaddingLeft(10);
				purposeValue.setHorizontalAlignment(Element.ALIGN_CENTER);
				purposeValue.setVerticalAlignment(Element.ALIGN_CENTER);
				purposeValue.setBackgroundColor(BaseColor.WHITE);
				purposeValue.setExtraParagraphSpace(5f);
				table.addCell(purposeValue);
				
				PdfPCell fromValue = new PdfPCell(new Paragraph(product.getBrand(), tableBody));
				fromValue.setBorderColor(BaseColor.BLACK);
				fromValue.setPaddingLeft(10);
				fromValue.setHorizontalAlignment(Element.ALIGN_CENTER);
				fromValue.setVerticalAlignment(Element.ALIGN_CENTER);
				fromValue.setBackgroundColor(BaseColor.WHITE);
				fromValue.setExtraParagraphSpace(5f);
				table.addCell(fromValue);
				
				PdfPCell toValue = new PdfPCell(new Paragraph(product.getMadein(), tableBody));
				toValue.setBorderColor(BaseColor.BLACK);
				toValue.setPaddingLeft(10);
				toValue.setHorizontalAlignment(Element.ALIGN_CENTER);
				toValue.setVerticalAlignment(Element.ALIGN_CENTER);
				toValue.setBackgroundColor(BaseColor.WHITE);
				toValue.setExtraParagraphSpace(5f);
				table.addCell(toValue);
				
				PdfPCell modeValue = new PdfPCell(new Paragraph(product.getMode(), tableBody));
				modeValue.setBorderColor(BaseColor.BLACK);
				modeValue.setPaddingLeft(10);
				modeValue.setHorizontalAlignment(Element.ALIGN_CENTER);
				modeValue.setVerticalAlignment(Element.ALIGN_CENTER);
				modeValue.setBackgroundColor(BaseColor.WHITE);
				modeValue.setExtraParagraphSpace(5f);
				table.addCell(modeValue);
				
				PdfPCell submittedByValue = new PdfPCell(new Paragraph(product.getSubmittedby(), tableBody));
				submittedByValue.setBorderColor(BaseColor.BLACK);
				submittedByValue.setPaddingLeft(10);
				submittedByValue.setHorizontalAlignment(Element.ALIGN_CENTER);
				submittedByValue.setVerticalAlignment(Element.ALIGN_CENTER);
				submittedByValue.setBackgroundColor(BaseColor.WHITE);
				submittedByValue.setExtraParagraphSpace(5f);
				table.addCell(submittedByValue);
				
				PdfPCell approvedByValue = new PdfPCell(new Paragraph(product.getApprovedby(), tableBody));
				approvedByValue.setBorderColor(BaseColor.BLACK);
				approvedByValue.setPaddingLeft(10);
				approvedByValue.setHorizontalAlignment(Element.ALIGN_CENTER);
				approvedByValue.setVerticalAlignment(Element.ALIGN_CENTER);
				approvedByValue.setBackgroundColor(BaseColor.WHITE);
				approvedByValue.setExtraParagraphSpace(5f);
				table.addCell(approvedByValue);
				
				PdfPCell paidByValue = new PdfPCell(new Paragraph(product.getPaidby(), tableBody));
				paidByValue.setBorderColor(BaseColor.BLACK);
				paidByValue.setPaddingLeft(10);
				paidByValue.setHorizontalAlignment(Element.ALIGN_CENTER);
				paidByValue.setVerticalAlignment(Element.ALIGN_CENTER);
				paidByValue.setBackgroundColor(BaseColor.WHITE);
				paidByValue.setExtraParagraphSpace(5f);
				table.addCell(paidByValue);
				
				PdfPCell noteValue = new PdfPCell(new Paragraph(product.getNote(), tableBody));
				noteValue.setBorderColor(BaseColor.BLACK);
				noteValue.setPaddingLeft(10);
				noteValue.setHorizontalAlignment(Element.ALIGN_CENTER);
				noteValue.setVerticalAlignment(Element.ALIGN_CENTER);
				noteValue.setBackgroundColor(BaseColor.WHITE);
				noteValue.setExtraParagraphSpace(5f);
				table.addCell(noteValue);
				
			}
			
			document.add(table);
			document.close();
			writer.close();
			return true;
			
		}catch(Exception e){
			return false;
		}
	}

	public boolean createExcel(List<Product> listBills, ServletContext context, HttpServletRequest request,
			HttpServletResponse response) {
			String filePath = context.getRealPath("/resources/reports");
			File file = new File(filePath);
			boolean exists = new File(filePath).exists();
			if(!exists) {
				new File(filePath).mkdirs();
			}
			
			try {
				FileOutputStream outputStream = new FileOutputStream(file+"/"+"listBills"+".xls");
				HSSFWorkbook workBook = new HSSFWorkbook();
				HSSFSheet workSheet = workBook.createSheet("listBills");
				workSheet.setDefaultColumnWidth(30);
				
				HSSFCellStyle headerCellStyle = workBook.createCellStyle();
				headerCellStyle.setFillForegroundColor(HSSFColor.BLUE.index);
				headerCellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
				
				HSSFRow headerRow = workSheet.createRow(0);
				
				HSSFCell date = headerRow.createCell(0);
				date.setCellValue("Date And Time");
				date.setCellStyle(headerCellStyle);
				
				HSSFCell purpose = headerRow.createCell(1);
				purpose.setCellValue("Purpose");
				purpose.setCellStyle(headerCellStyle);
				
				HSSFCell from = headerRow.createCell(2);
				from.setCellValue("From");
				from.setCellStyle(headerCellStyle);
				
				HSSFCell to = headerRow.createCell(3);
				to.setCellValue("To");
				to.setCellStyle(headerCellStyle);
				
				HSSFCell mode = headerRow.createCell(4);
				mode.setCellValue("Mode");
				mode.setCellStyle(headerCellStyle);
				
				HSSFCell price = headerRow.createCell(5);
				price.setCellValue("Price");
				price.setCellStyle(headerCellStyle);
				
				HSSFCell paidBy = headerRow.createCell(6);
				paidBy.setCellValue("Paid By");
				paidBy.setCellStyle(headerCellStyle);
				
				HSSFCell approvedBy = headerRow.createCell(7);
				approvedBy.setCellValue("Approved By");
				approvedBy.setCellStyle(headerCellStyle);
				
				HSSFCell submittedBy = headerRow.createCell(8);
				submittedBy.setCellValue("Submitted By");
				submittedBy.setCellStyle(headerCellStyle);
				
				HSSFCell note = headerRow.createCell(9);
				note.setCellValue("Note");
				note.setCellStyle(headerCellStyle);
				
				int i = 1;
				
				for(Product product : listBills) {
					HSSFRow bodyRow = workSheet.createRow(i);
					
					HSSFCellStyle bodyCellStyle = workBook.createCellStyle();
					bodyCellStyle.setFillBackgroundColor(HSSFColor.WHITE.index);
					
					HSSFCell dateValue = bodyRow.createCell(0);
					dateValue.setCellValue(product.getDate());
					dateValue.setCellStyle(bodyCellStyle);
					
					HSSFCell purposeValue = bodyRow.createCell(1);
					purposeValue.setCellValue(product.getPurpose());
					purposeValue.setCellStyle(bodyCellStyle);
					
					HSSFCell fromValue = bodyRow.createCell(2);
					fromValue.setCellValue(product.getMadein());
					fromValue.setCellStyle(bodyCellStyle);
					
					HSSFCell toValue = bodyRow.createCell(3);
					toValue.setCellValue(product.getMadein());
					toValue.setCellStyle(bodyCellStyle);
					
					HSSFCell modeValue = bodyRow.createCell(4);
					modeValue.setCellValue(product.getMode());
					modeValue.setCellStyle(bodyCellStyle);
					
					HSSFCell priceValue = bodyRow.createCell(5);
					priceValue.setCellValue(product.getPrice());
					priceValue.setCellStyle(bodyCellStyle);
					
					HSSFCell paidByValue = bodyRow.createCell(6);
					paidByValue.setCellValue(product.getPaidby());
					paidByValue.setCellStyle(bodyCellStyle);
					
					HSSFCell approvedByValue = bodyRow.createCell(7);
					approvedByValue.setCellValue(product.getApprovedby());
					approvedByValue.setCellStyle(bodyCellStyle);
					
					HSSFCell submittedByValue = bodyRow.createCell(8);
					submittedByValue.setCellValue(product.getSubmittedby());
					submittedByValue.setCellStyle(bodyCellStyle);
					
					HSSFCell noteValue = bodyRow.createCell(9);
					noteValue.setCellValue(product.getNote());
					noteValue.setCellStyle(bodyCellStyle);
					
					i++;
				}
				
				workBook.write(outputStream);
				outputStream.flush();
				outputStream.close();
				return true;
				
			}catch(Exception e){
				
				return false;
				
			}
	}
	
}
