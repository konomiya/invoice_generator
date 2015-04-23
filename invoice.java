import java.io.*;
import java.time.*;
import java.time.format.*;
 
import javax.servlet.*;
import javax.servlet.http.*;

import org.eclipse.jetty.server.*;
import org.eclipse.jetty.server.handler.*;
import org.eclipse.jetty.servlet.*;
 
import com.itextpdf.text.*;
import com.itextpdf.text.BaseColor.*;
import com.itextpdf.text.pdf.*;
import java.sql.*;
import java.text.*;
import java.util.*;

public class CimplestServer
{
    public static void main( String[] args ) throws Exception
    {
        Server server = new Server(<port redacted>);

        ContextHandler m1 = new ContextHandler();
        m1.setContextPath("/m1");
        m1.setClassLoader(Thread.currentThread().getContextClassLoader());

        ContextHandler m2 = new ContextHandler();
        m2.setContextPath("/m2");
        m2.setClassLoader(Thread.currentThread().getContextClassLoader());

        ContextHandlerCollection contexts = new ContextHandlerCollection();
        contexts.setHandlers(new Handler[] { m1, m2, new DefaultHandler() });
 
        server.setHandler(contexts);
 
        m1.setHandler(new M1());
        m2.setHandler(new M2());

        server.start();
        server.join();
    }
}

class M1 extends AbstractHandler
{
    public void handle(String target,Request baseRequest,HttpServletRequest request,HttpServletResponse response) 
        throws IOException, ServletException
    {
        try {
            Class.forName("org.postgresql.Driver").newInstance();
            String url = "jdbc:postgresql://<hostname:port redacted>/<database redacted>"
            + "?allowEncodingChanges=true";
            Connection connection = DriverManager.getConnection(url, <redacted>, <redacted>);
            PreparedStatement pstatement1 = connection.prepareStatement(<redacted>);
            pstatement1.setString(1, param_1);
            pstatement1.setString(.., param..);
            pstatement1.setString(n, param_n);
            ResultSet rs = pstatement1.executeQuery();
            if(!rs.isBeforeFirst()) {
                response.setContentType("text/html; charset=UTF-8");
                response.getWriter().write("NO DATA.");
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                baseRequest.setHandled(true);
                return;
            }

            Document document = new Document(PageSize.B5.rotate(), 10, 10, 60, 60);
            BaseFont bf = BaseFont.createFont("KozMinPro-Regular", "UniJIS-UCS2-H", BaseFont.EMBEDDED);
            Font titleFont = FontFactory.getFont("Times-Roman");
            FontFactory.register("./Anonymous Pro.ttf", "apro");
            Font cfont = FontFactory.getFont("apro", 10);
            FontFactory.register("./Anonymous Pro B.ttf", "aprob");
            Font cbfont = FontFactory.getFont("aprob", 12);
            Font jfont = new com.itextpdf.text.Font(bf,10);
            Font bfont = new Font(bf, 14, Font.BOLD);
            ServletOutputStream servletOut = response.getOutputStream();
 
            PdfWriter writer = PdfWriter.getInstance(document, servletOut);
            HeaderFooter event = new HeaderFooter();
            writer.setBoxSize("art", new Rectangle(36, 54, 788, 559));
            writer.setPageEvent(event);

            PdfPTable t0 = new PdfPTable(10);
            t0.setTotalWidth(new float[]{30f,80f,70f,70f,60f,70f,70f,70f,60f,80f});
            t0.getDefaultCell().setFixedHeight(20);
            t0.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            t0.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
            t0.setLockedWidth(true);
   
            t0.addCell(new Phrase(col1, jfont));
            t0.addCell(new Phrase(col_n, jfont));

            t0.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
            t0.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

            PdfPTable t1 = new PdfPTable(8);
            t1.setTotalWidth(new float[]{60f,60f,40f,80f,80f,80f,80f,180f});
            t1.getDefaultCell().setFixedHeight(20);
            t1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            t1.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
            t1.setLockedWidth(true);

            t1.addCell(new Phrase(col_1, jfont));
            t1.addCell(new Phrase(col_n, jfont));

            t1.setHeaderRows(1);

            PdfPCell c;

            document.open();

            int row = 0;

            while(rs.next()) {
                if(row == 0) {
                    event.setHeader(header_1,
                                    rs.getString(record.1 etc));

                     t0.addCell(new Phrase(col_1, jfont));
                     t0.addCell(new Phrase(col_2, cbfont));
                     row++;
                     continue;
                 }
                 c = new PdfPCell(new Phrase("", jfont));
                 c.setHorizontalAlignment(Element.ALIGN_CENTER);
                 c.setVerticalAlignment(Element.ALIGN_MIDDLE);
                 t1.addCell(c);
                 t1.addCell(new PdfPCell(new Phrase("", jfont)));
                 c = new PdfPCell(new Phrase(rs.getString(""), cfont));
                 c.setHorizontalAlignment(Element.ALIGN_RIGHT);
                 c.setVerticalAlignment(Element.ALIGN_MIDDLE);
                 t1.addCell(c);
                 c = new PdfPCell(new Phrase(rs.getString(""), cfont));
                 c.setHorizontalAlignment(Element.ALIGN_RIGHT);
                 c.setVerticalAlignment(Element.ALIGN_MIDDLE);
                 t1.addCell(c);
                 c.setHorizontalAlignment(Element.ALIGN_RIGHT);
                 c.setVerticalAlignment(Element.ALIGN_MIDDLE);
                 t1.addCell(c);
                 row++;
            }

            document.add(t0);
            document.add(new Phrase("", jfont));
            document.add(t1);
            document.close();

            connection.close();

            response.setContentType("application/octet-stream");
            if(download) {
                response.setHeader("Content-Disposition","attachment; filename=x.pdf");
            }
            else {
                response.setHeader("Content-Disposition","inline; filename=y.pdf");
            }
            response.setHeader("Cache-Control", "private");
            response.setStatus(HttpServletResponse.SC_OK);
            baseRequest.setHandled(true);
        }

        catch (InstantiationException e)
        {
            e.printStackTrace();
        }
        catch (IllegalArgumentException e)
        {
                response.setContentType("text/html; charset=UTF-8");
                response.getWriter().write("NO DATA.");
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                baseRequest.setHandled(true);
                return;
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        catch (DocumentException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}

class M2 extends AbstractHandler
{
    public void handle(String target,Request baseRequest,HttpServletRequest request,HttpServletResponse response)
        throws IOException, ServletException
    {
        BaseColor bc = new BaseColor(240,240,240);
        BaseColor bc2 = new BaseColor(180,180,180);
        Float padd = 0.0f;

        try {
            Class.forName("org.postgresql.Driver").newInstance();
            String url = "jdbc:postgresql://<redacted>/<redacted>?allowEncodingChanges=true";
            Connection connection = DriverManager.getConnection(url, <redacted>, <redacted>);
            PreparedStatement pstatement1 = connection.prepareStatement(<redacted>);
            pstatement1.setString(1, param_1);
            pstatement1.setString(.., ..);
            pstatement1.setString(n, param_n);
            ResultSet rs = pstatement1.executeQuery();
            if(!rs.isBeforeFirst()) {
                response.setContentType("text/html; charset=UTF-8");
                response.getWriter().write("NO DATA.");
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                baseRequest.setHandled(true);
                return;
            }

            Document document = new Document(PageSize.B5.rotate(), 10, 10, 60, 60);
            BaseFont bf = BaseFont.createFont("KozMinPro-Regular", "UniJIS-UCS2-H", BaseFont.EMBEDDED);
            Font titleFont = FontFactory.getFont("Times-Roman");
            FontFactory.register("./Anonymous Pro.ttf", "apro");
            Font cfont = FontFactory.getFont("apro", 7);
            FontFactory.register("./Anonymous Pro B.ttf", "aprob");
            Font cbfont = FontFactory.getFont("aprob", 10);
            Font jfont = new com.itextpdf.text.Font(bf,7);
            Font bfont = new Font(bf, 12, Font.BOLD);
            ServletOutputStream servletOut = response.getOutputStream();

            PdfWriter writer = PdfWriter.getInstance(document, servletOut);
            HeaderFooter event = new HeaderFooter();
            writer.setBoxSize("art", new Rectangle(36, 54, 788, 559));
            writer.setPageEvent(event);

            PdfPTable t1 = new PdfPTable(<redacted>);
            t1.getDefaultCell().setPadding(padd);
            t1.getDefaultCell().setBorderColor(bc2);
            t1.setTotalWidth(new float[]{<redacted>});
            t1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            t1.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
            t1.setLockedWidth(true);

            t1.addCell(new Phrase("", jfont));
            t1.addCell(new Phrase("", jfont));

            t1.setHeaderRows(1);

            PdfPCell c;

            float[][] x = {
                { document.left(), document.left() + 380 },
                { document.right() - 380, document.right() }
            };

            document.open();

            ColumnText column = new ColumnText(writer.getDirectContent());

            String prevTenpo = "";
            String prevBumon = "";

            String finalTotal = "";

            while(rs.next()) {
                if(rs.isFirst()) {
                    event.setHeader("", 
                                    rs.getString("") + rs.getString(""),
                                    rs.getString(""),
                                    rs.getString(""),
                                    rs.getString(""));
                }
                t1.addCell(new Phrase(rs.getString(""), jfont));
                t1.addCell(new Phrase(rs.getString(""), jfont));
                c = new PdfPCell(new Phrase(rs.getString(""), cfont));
                c.setPadding(padd);
                c.setBorderColor(bc2);
                c.setHorizontalAlignment(Element.ALIGN_RIGHT);
                c.setVerticalAlignment(Element.ALIGN_MIDDLE);
                t1.addCell(c);
                c = new PdfPCell(new Phrase(rs.getString(""), cfont));
                c.setPadding(padd);
                c.setBorderColor(bc2);
                c.setHorizontalAlignment(Element.ALIGN_RIGHT);
                c.setVerticalAlignment(Element.ALIGN_MIDDLE);
                t1.addCell(c);
                t1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                t1.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
                t1.addCell(new Phrase("", cfont));
                finalTotal = rs.getString("");
            }
            c = new PdfPCell(new Phrase("", bfont));
            c.setColspan(3);
            c.setBorderColor(bc2);
            t1.addCell(c);
            c = new PdfPCell(new Phrase(finalTotal + "", bfont));
            c.setHorizontalAlignment(Element.ALIGN_RIGHT);
            c.setColspan(3);
            c.setBorderColor(bc2);
            t1.addCell(c);

            boolean b = true;
            for(PdfPRow r : t1.getRows()) {
               for(PdfPCell cx: r.getCells()) {
                  if(cx == null) {
                      continue;
                  }
                  cx.setBackgroundColor(b ? bc : BaseColor.WHITE);
               }
               b = !b;
            }

            column.addElement(t1);

            int count = 0;
            float height = 0;
            int status = ColumnText.START_COLUMN;
            // render the column as long as it has content
            while (ColumnText.hasMoreText(status)) {
                // set the dimensions of the current column
                column.setSimpleColumn(
                    x[count][0], document.bottom(),
                    x[count][1], document.top() - height - 10);
                // render as much content as possible
                status = column.go();
                // go to a new page if you've reached the last column
                if (++count > 1) {
                    count = 0;
                    document.newPage();
                }
            }

            document.close();

            connection.close();

            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition","inline;filename=z.pdf");
            response.setHeader("Cache-Control" , "private");
            response.setStatus(HttpServletResponse.SC_OK);
            baseRequest.setHandled(true);
        }

        catch (InstantiationException e)
        {
            e.printStackTrace();
        }
        catch (IllegalArgumentException e)
        {
                response.setContentType("text/html; charset=UTF-8");
                response.getWriter().write("NO DATA.");
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                baseRequest.setHandled(true);
                return;
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        catch (DocumentException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}

class HeaderFooter extends PdfPageEventHelper {
        int pagenumber;
        int totalpages;
        String title1;
        String title2;
        String whatever;
        protected PdfTemplate total;
        protected BaseFont helv;
        protected BaseFont shelv;
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        public void setHeader(String title1, String title2, String whatever) {
            this.title1 = title1;
            this.title2 = title2;
            this.whatever = whatever;
        }

        public void onOpenDocument(PdfWriter writer, Document document) {
            total = writer.getDirectContent().createTemplate(100, 100);
            total.setBoundingBox(new Rectangle(-20, -20, 100, 100));
            try {
                helv = BaseFont.createFont("KozMinPro-Regular", "UniJIS-UCS2-H", BaseFont.EMBEDDED);
                shelv = BaseFont.createFont("KozMinPro-Regular", "UniJIS-UCS2-H", BaseFont.EMBEDDED);
            } catch (Exception e) {
                throw new ExceptionConverter(e);
            }
        }

        public void onChapter(PdfWriter writer,
                Document document,
                float paragraphPosition,
                Paragraph title) {
            pagenumber = 1;
        }

        public void onChapterEnd(PdfWriter writer,
                Document document,
                float position) {
        }

        public void onStartPage(PdfWriter writer, Document document) {
            pagenumber++;
        }

        public void onEndPage(PdfWriter writer, Document document) {
            Rectangle rect = writer.getBoxSize("art");
            PdfContentByte cb = writer.getDirectContent();
            cb.saveState();

            String text = "Page " + (pagenumber) + "/";
            float textBase = document.bottom() - 3;
            float textSize = helv.getWidthPoint(text, 6);
            cb.beginText();
            cb.setFontAndSize(helv, 6);

            {
                float adjust = helv.getWidthPoint(Integer.toString(pagenumber), 6);
                float adjust2 = helv.getWidthPoint(Integer.toString(totalpages), 6);
                cb.setTextMatrix(document.right() - textSize - adjust-28, textBase-40);
                cb.showText(text);
                cb.endText();
                cb.addTemplate(total, document.right() - adjust-28, textBase-40);
            }
            cb.restoreState();

            BaseFont bfr = null, bfb = null;
            try
            {
                bfr = BaseFont.createFont("KozMinPro-Regular", "UniJIS-UCS2-H", BaseFont.EMBEDDED);
                bfb = BaseFont.createFont("KozMinPro-Regular", "UniJIS-UCS2-H", BaseFont.EMBEDDED);
            }
            catch (DocumentException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            Font bfont = new Font(bfb, 14, Font.BOLD);
            Font bufont = new Font(bfb, 12, Font.BOLD | Font.UNDERLINE);
            Font rfont = new Font(bfr, 10);
            ColumnText.showTextAligned(writer.getDirectContent(),
                    Element.ALIGN_LEFT, new Phrase("Company1", bfont),
                    rect.getLeft(), rect.getBottom()+420, 0);
            ColumnText.showTextAligned(writer.getDirectContent(),
                    Element.ALIGN_LEFT, new Phrase("Company2", rfont),
                    rect.getLeft(), rect.getBottom()+400, 0);
            ColumnText.showTextAligned(writer.getDirectContent(),
                    Element.ALIGN_LEFT, new Phrase("Address1", rfont),
                    rect.getLeft()+200, rect.getBottom()+400, 0);
            ColumnText.showTextAligned(writer.getDirectContent(),
                    Element.ALIGN_LEFT, new Phrase("Address2" + shdt, bufont),
                    rect.getLeft()+460, rect.getBottom()+400, 0);
            bufont = new Font(bfb, 12, Font.BOLD | Font.UNDERLINE);
            ColumnText.showTextAligned(writer.getDirectContent(),
                    Element.ALIGN_RIGHT, new Phrase(title, bufont),
                    rect.getLeft()+380, rect.getBottom()+420, 0);
            rfont = new Font(bfr, 8);
            ColumnText.showTextAligned(writer.getDirectContent(),
                    Element.ALIGN_RIGHT, new Phrase(sdf.format(Calendar.getInstance().getTime()), rfont),
                    rect.getRight()-120, rect.getBottom()+420, 0);
            ColumnText.showTextAligned(writer.getDirectContent(),
                    Element.ALIGN_RIGHT, new Phrase("", rfont),
                    rect.getRight()-100, rect.getBottom() - 12, 0);
            ColumnText.showTextAligned(writer.getDirectContent(),
                    Element.ALIGN_RIGHT, new Phrase("TEL: xxx", rfont),
                    rect.getRight()-100, rect.getBottom() - 20, 0);
            ColumnText.showTextAligned(writer.getDirectContent(),
                    Element.ALIGN_RIGHT, new Phrase("FAX: xxx", rfont),
                    rect.getRight()-100, rect.getBottom() - 28, 0);
        }

        public void onCloseDocument(PdfWriter writer, Document document) {
            total.beginText();
            total.setFontAndSize(helv, 6);
            total.setTextMatrix(0, 0);
            totalpages = writer.getPageNumber() - 1;
            total.showText(String.valueOf(totalpages));
            total.endText();
    }
}
