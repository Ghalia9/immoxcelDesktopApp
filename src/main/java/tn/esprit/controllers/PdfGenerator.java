package tn.esprit.controllers;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.border.DashedBorder;
import com.itextpdf.layout.border.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import tn.esprit.models.Supplier;
import tn.esprit.models.Transaction;
import tn.esprit.services.ServiceSupplier;
import tn.esprit.services.ServiceTransaction;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PdfGenerator {
    public Date currentDate=new Date();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    String currentDateTime = dateFormat. format(currentDate);

    private final ServiceTransaction sp = new ServiceTransaction();


    public void GeneratePDFTransaction(int idTransaction) throws FileNotFoundException, MalformedURLException {
        Transaction transaction= sp.getOneById(idTransaction);
        String TextValue= String.valueOf(transaction.getId());
        String quantity=String.valueOf((transaction.getQuantity()));
        String totalAmount=String.valueOf((transaction.getCost()));

        String path ="invoice.pdf";
        PdfWriter pdfWriter= new PdfWriter(path);
        PdfDocument pdfDocument=new PdfDocument(pdfWriter);
        pdfDocument.setDefaultPageSize(PageSize.A4);


        Document document= new Document(pdfDocument);

        String imagePath="C:\\Users\\Alice\\IdeaProjects\\MyJavaFxApp\\src\\main\\resources\\com\\example\\myjavafxapp\\Pictures\\logo-ct.png";
        ImageData imageData=  ImageDataFactory.create(imagePath);
        Image image = new Image(imageData);
        float x= pdfDocument.getDefaultPageSize().getWidth()/2;
        float y= pdfDocument.getDefaultPageSize().getHeight()/2;
        image.setFixedPosition(x-268,y-160);
        image.setOpacity(0.1f);
        document.add(image);


        Paragraph onesp=new Paragraph("\n");
        Paragraph onesp1=new Paragraph("\n" + "\n"+ "\n"+ "\n"+ "\n"+ "\n"+ "\n");

        float threecol=190f;
        float twocol=285f;
        float twocol150=twocol+150f;
        float twocolumnWidth[]={twocol150,twocol};
        float fullwidth[]={threecol*3};
        float threeColumnWidth []= {threecol,threecol,threecol};

        Table table = new Table(twocolumnWidth);
        table.addCell(getTransaction("Transaction"));
        Table nestedtable= new Table( new float[]{ twocol/2,twocol/2});
        nestedtable.addCell(getHeaderTextCell("Transaction No°"));
        nestedtable.addCell(getHeaderTextCellValue(TextValue));
        nestedtable.addCell(getHeaderTextCell("Date: "));
        nestedtable.addCell(getHeaderTextCellValue(currentDateTime));
        table.addCell(new Cell().add(nestedtable).setBorder(Border.NO_BORDER));

        Border gb = new SolidBorder(com.itextpdf.kernel.color.Color.BLACK, 1f/2f);
        Table divider= new Table (fullwidth);
        divider.setBorder(gb);
        document.add(table);
        document.add(onesp);
        document.add(divider);
        document.add(onesp);
        Table bodyTable= new Table( twocolumnWidth);
        bodyTable.addCell(getHeaderTextCell("About Immoxcel "));
        document.add(bodyTable.setMarginBottom(12f));

        Table BodyTableContent= new Table( twocolumnWidth);

        BodyTableContent.addCell(getCell10toLeft("Company ",true));
        BodyTableContent.addCell(getCell10toLeft("Phone ",true));
        BodyTableContent.addCell(getCell10toLeft("Immoxcel ",false));
        BodyTableContent.addCell(getCell10toLeft("+216********  ",false));
        document.add(BodyTableContent);

        Table bodytableContent2= new Table( twocolumnWidth);

        bodytableContent2.addCell(getCell10toLeft("Address ",true));
        bodytableContent2.addCell(getCell10toLeft("Region Code ",true));
        bodytableContent2.addCell(getCell10toLeft("Sousse Msaken vilette 3eme block ",false));
        bodytableContent2.addCell(getCell10toLeft("4070  ",false));
        document.add(bodytableContent2);

        float oneColumnwidth[]={twocol150};

        Table oneColTab1= new Table( oneColumnwidth);

      //  oneColTab1.addCell(getCell10toLeft("Company ",true));
//        oneColTab1.addCell(getCell10toLeft("Name ",true));
        oneColTab1.addCell(getCell10toLeft("Email ",true));
        oneColTab1.addCell(getCell10toLeft("Immoxcel@yandex.com  ",false));
        document.add(oneColTab1.setMarginBottom(10f));

        Table tableDivider2= new Table(fullwidth);
        Border dgb= new DashedBorder(com.itextpdf.kernel.color.Color.BLACK,0.5f);
        document.add(onesp);
        document.add(tableDivider2.setBorder(dgb));
        document.add(onesp);

        Paragraph prductText=new Paragraph("The Transaction Details ");
        document.add(prductText.setBold().setFontColor(com.itextpdf.kernel.color.Color.RED));

        Table threeColTab= new Table(threeColumnWidth);
        threeColTab.setBackgroundColor(com.itextpdf.kernel.color.Color.BLACK,0.7f);
        threeColTab.addCell(titleColorsFont("Description "));
        threeColTab.addCell(titleColorsCenter("Quantity "));
        threeColTab.addCell(titleColorsCenterWithMergine("Price"));
        document.add(threeColTab);

        Table threeColTabContent= new Table(threeColumnWidth);
        threeColTabContent.addCell(getCell10toLeft(transaction.getDescription(),false));
        threeColTabContent.addCell(getCell10toLeft(quantity,false));
        threeColTabContent.addCell(getCell10toLeft(totalAmount,false));
        document.add(threeColTabContent);

        float oneTwo[]={threecol+125f,threecol*2};
        Table threeColTable4= new Table(oneTwo);
       // threeColTable4.addCell(getCell10toLeft(totalAmount,false));
        document.add(threeColTable4);
        document.add(divider);
        document.add(onesp);


        Table tb= new Table(fullwidth);
        document.add(onesp1);

        tb.addCell(new Cell().add("TERMS AND CONDITIONS \n").setBold().setBorder(Border.NO_BORDER).setFontColor(com.itextpdf.kernel.color.Color.RED));
        document.add(onesp);
        List<String> tncList=new ArrayList<>();
        tncList.add("1.The Seller shall not be liable to the buyer directly or indirectly for any loss or damage suffered from the Buyer");
        tncList.add("2. The Seller warrants the product for one (1)  year from the date of shipment");
        tncList.add("\n");
        tncList.add("\n");
        tncList.add("\n");
        tncList.add("Sign Here");
        tncList.add("       .    ");


        for (String tnc : tncList){
            tb.addCell(new Cell().add(tnc).setBorder(Border.NO_BORDER));
        }
        document.add(tb);
        document.close();
    }



    public void GeneratePDFSupplier(String companyName,String address,String prefixLabel, String  phone,String patentRef) throws FileNotFoundException {

        System.out.println("date in pdf generator function eqauls to "+currentDateTime);
        String path ="SupplierContract.pdf";
        PdfWriter pdfWriter= new PdfWriter(path);
        PdfDocument pdfDocument=new PdfDocument(pdfWriter);
        pdfDocument.setDefaultPageSize(PageSize.A4);
        Document document= new Document(pdfDocument);
        Paragraph onesp=new Paragraph("\n");

        float threecol=190f;
        float twocol=285f;
        float twocol150=twocol+150f;
        float twocolumnWidth[]={twocol150,twocol};
        float fullwidth[]={threecol*3};
        float threeColumnWidth []= {threecol,threecol,threecol};
        Table table = new Table(twocolumnWidth);
        table.addCell(getTransaction("Contract"));
        Table nestedtable= new Table( new float[]{ twocol/2,twocol/2});
        nestedtable.addCell(getHeaderTextCell(" Company"));
        nestedtable.addCell(getHeaderTextCellValue("Immoxcel"));
        nestedtable.addCell(getHeaderTextCell("Date: "));
        nestedtable.addCell(getHeaderTextCellValue(currentDateTime));
        table.addCell(new Cell().add(nestedtable).setBorder(Border.NO_BORDER));

        Border gb = new SolidBorder(com.itextpdf.kernel.color.Color.BLACK, 1f/2f);
        Table divider= new Table (fullwidth);
        divider.setBorder(gb);
        document.add(table);
        document.add(onesp);
        document.add(divider);
        document.add(onesp);
//        Table bodyTable= new Table( twocolumnWidth);
//        bodyTable.addCell(getHeaderTextCell("Party One  "));
//        bodyTable.addCell(getHeaderTextCell("Party Two  "));
//        document.add(bodyTable.setMarginBottom(12f));

        Table BodyTableContent= new Table( twocolumnWidth);

        BodyTableContent.addCell(getCell10toLeft("Company ",true));
        BodyTableContent.addCell(getCell10toLeft("Company ",true));
        BodyTableContent.addCell(getCell10toLeft("Immoxcel ",false));
        BodyTableContent.addCell(getCell10toLeft(companyName,false));
        document.add(BodyTableContent);

        Table bodytableContent2= new Table( twocolumnWidth);
        String Phone = prefixLabel + String.valueOf(phone);
        bodytableContent2.addCell(getCell10toLeft("Phone  ",true));
        bodytableContent2.addCell(getCell10toLeft("Phone ",true));
        bodytableContent2.addCell(getCell10toLeft("+216 99 *** ***  ",false));
        bodytableContent2.addCell(getCell10toLeft(Phone,false));
        document.add(bodytableContent2);

        float oneColumnwidth[]={twocol150};

        Table oneColTab1= new Table( oneColumnwidth);
        oneColTab1.addCell(getCell10toLeft("Email ",true));
        oneColTab1.addCell(getCell10toLeft("Immoxcel@yandex.com  ",false));
        document.add(oneColTab1.setMarginBottom(10f));




        Table tableDivider2= new Table(fullwidth);
        Border dgb= new DashedBorder(com.itextpdf.kernel.color.Color.BLACK,0.5f);
        document.add(onesp);
        document.add(tableDivider2.setBorder(dgb));

        Table tb= new Table(fullwidth);

        tb.addCell(new Cell().add("TERMS AND CONDITIONS \n").setBold().setBorder(Border.NO_BORDER).setFontColor(com.itextpdf.kernel.color.Color.RED));
        document.add(onesp);
        List<String> tncList=new ArrayList<>();
        tncList.add("1.The Seller shall not be liable to the buyer directly or indirectly for any loss or damage suffered from the Buyer");
        tncList.add("2. The Seller warrants the product for one (1)  year from the date of shipment");
        tncList.add("3. The Buyer agrees to pay all applicable taxes and duties related to the purchase of goods or services.");
        tncList.add("4. All orders are subject to acceptance by the Seller, and the Seller reserves the right to refuse any order in its sole discretion.");
        tncList.add("5. Title to the goods shall pass to the Buyer upon delivery, unless otherwise specified in writing by the Seller.");
        tncList.add("6. The Buyer shall inspect the goods upon receipt and notify the Seller of any defects or discrepancies within seven (7) days of delivery.");
        tncList.add("7. The Seller shall have the right to remedy any non-conformities in the goods or services within a reasonable time frame.");
        tncList.add("8. The Buyer agrees to comply with all applicable laws and regulations governing the use or resale of the goods or services provided by the Seller.");
        tncList.add("9. This agreement constitutes the entire understanding between the parties and supersedes all prior agreements or understandings, whether oral or written.");
        tncList.add("10. Any modifications or amendments to this agreement must be made in writing and signed by both parties to be valid.");
        tncList.add("\n");
        tncList.add("Sign Here");



        for (String tnc : tncList){
            tb.addCell(new Cell().add(tnc).setBorder(Border.NO_BORDER));
        }
        document.add(tb);
        document.close();
    }
    static Cell getCell10toLeft(String textValue,Boolean isBold){
        Cell myCell = new Cell().add(textValue).setFontSize(10f).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT);
        return isBold ?myCell.setBold():myCell;
    }


    static Cell getHeaderTextCell(String textValue){
        return new Cell().add(textValue).setBold().setBorder(Border.NO_BORDER);
    }

    static Cell getHeaderTextCellValue(String textValue){
        return new Cell().add(textValue).setBold().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT);
    }
    static Cell getTransaction(String textValue){
        return new Cell().add(textValue).setFontSize(20f).setBold().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT);
    }

    static Cell getDetails(String textValue){
        return new Cell().add(textValue).setFontSize(12f).setBold().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT);
    }

    Cell titleColorsFont(String textValue){
        return new Cell().add(textValue).setFontSize(12f).setBold().setBorder(Border.NO_BORDER).setFontColor(com.itextpdf.kernel.color.Color.WHITE);
    }
    Cell titleColorsCenter(String textValue){
        return new Cell().add(textValue).setFontSize(12f).setBold().setBorder(Border.NO_BORDER).setFontColor(com.itextpdf.kernel.color.Color.WHITE).setTextAlignment(TextAlignment.CENTER);
    }
    Cell titleColorsCenterWithMergine(String textValue){
        return new Cell().add(textValue).setFontSize(12f).setBold().setBorder(Border.NO_BORDER).setFontColor(com.itextpdf.kernel.color.Color.WHITE).setTextAlignment(TextAlignment.RIGHT).setMarginRight(15f);
    }

}
