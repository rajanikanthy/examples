package demos;

import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.cxf.io.CachedOutputStream;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.AttachmentBuilder;
import org.apache.cxf.jaxrs.ext.multipart.ContentDisposition;
import org.apache.cxf.jaxrs.ext.multipart.MultipartBody;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

@Path("/")
public class HelloService {
	@GET
	@Path("/hello")
	public Response sayHello() {
		return Response.ok("Hello World").build();
	}
	
	
	@GET
	@Path("/generate")
	@Produces("multipart/mixed")
	public MultipartBody generate() {
		Workbook wb = new HSSFWorkbook();
		Sheet sheet = wb.createSheet("Test");
		Row row = sheet.createRow(0);
		row.createCell(0).setCellValue("Hello World");
		OutputStream outputStream = new CachedOutputStream();
		try {
			wb.write(outputStream);
			AttachmentBuilder attachmentBuilder = new AttachmentBuilder();
			ContentDisposition cd = new ContentDisposition("attachment;filename=test.xls");
			attachmentBuilder.contentDisposition(cd);
			attachmentBuilder.id("file");
			attachmentBuilder.header("Content-Type", "application/vnd.ms-excel");
			List<Attachment> atts = new LinkedList<Attachment>();
			attachmentBuilder.object(((CachedOutputStream)outputStream).getInputStream());
			atts.add(attachmentBuilder.build());
			return new MultipartBody(attachmentBuilder.build());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
