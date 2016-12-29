package com.Rohan;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;


import java.io.*;
import java.util.LinkedList;
import java.util.Scanner;


/**
 * Created by Rohan on 12/29/2016.
 */


public class Watermarker {

    private static final String RESULT = "C://Users//Rohan//Desktop//itextTestBed+Jars/watermarkedDocument.pdf";
    private static final String SRC = "C://Users//Rohan//Desktop//itextTestBed+Jars/RohanPurohitResumeDec2016unprocessed.pdf";
        LinkedList<String> customizableStamper = new LinkedList<>();
        String stamp1;
        String stamp2;
        String stamp3;

        public static void main(String[] args) throws IOException,DocumentException {
            Watermarker ab= new Watermarker();
            byte[] inputByteArray=ab.getBytesFromFile(SRC);
            ab.designStamp("Rohan Purohit", "Approved","Dec29-2016");
            ab.writeBytestoFile(RESULT, ab.stampPdf(inputByteArray));

        }

    private byte[] getBytesFromFile(String src) throws IOException{
        FileInputStream fs=new FileInputStream(src);
        byte[] b= new byte[fs.available()];
        fs.read(b);
        return b;
    }

    private void writeBytestoFile(String dest, byte[] b) throws IOException{
        FileOutputStream fs=new FileOutputStream(dest);
        fs.write(b);
        fs.close();
    }

    public  LinkedList<String> designStamp(String stampVerbiage, String stampDate, String stampDiagonal){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter the comment of your custom stamp");
        this.stamp1 = scanner.nextLine();
        customizableStamper.add(0,this.stamp1);

        System.out.println("Please enter date for stamp");
        this.stamp2= scanner.nextLine();
        customizableStamper.add(1,this.stamp2);

        System.out.println("Please enter diagonal line for stamp");
        this.stamp3= scanner.nextLine();
        customizableStamper.add(2,this.stamp3);

        return customizableStamper;
    }




    public static double GetHypotenuseAngleInDegreesFrom(double opposite, double adjacent)
        {
            //Tan <angle> = opposite/adjacent{soh|cah|toa}
            double radians = Math.atan2(opposite, adjacent); // Get Radians for Atan2
            double angle = radians*(180/Math.PI); // Change back to degrees
            return angle;}


        public byte[] stampPdf(byte [] inputByteArray) throws IOException, DocumentException{

            byte[] stampedPdf;

            ByteArrayInputStream bais = new ByteArrayInputStream(inputByteArray);
            ByteArrayOutputStream baos =new ByteArrayOutputStream();
            System.out.println("About to assign inputStream to reader");
            PdfReader reader = new PdfReader(bais);
            System.out.println("About to stamp the streamed document by calling PDFStamper");
            PdfStamper stamper = new PdfStamper(reader, baos);
            System.out.println("About to stamp the streamed document by calling PDFStamper");
            System.out.println("Entering try catch block of pdf stamper");


            try{
                for(int i=1; i<= reader.getNumberOfPages(); i++){
                    Rectangle pageSize = reader.getPageSizeWithRotation(i);

                    //PdfContentByte over = stamper.getOverContent(i);
                    PdfContentByte underContent = stamper.getUnderContent(i);
                    PdfContentByte verbiage = stamper.getOverContent(i);
                  //  System.out.println("Now Created DocOutBO and made PDF mapping, going to do UUID mapping");

                    String a = null;
                    String b = null;
                    String e = null;

                    a = (String) customizableStamper.get(0);
                    b = (String) customizableStamper.get(1);
                    e = (String) customizableStamper.get(2);//diagonal verbiage

                    float textAngle =(float) GetHypotenuseAngleInDegreesFrom(pageSize.getHeight(), pageSize.getWidth());

                    PdfGState gs = new PdfGState();
                    gs.setFillOpacity(0.8f);
                    verbiage.beginText();

                    BaseFont baseFont = BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.WINANSI, BaseFont.EMBEDDED);
                    verbiage.setFontAndSize(baseFont, 13);
                    verbiage.setGState(gs);
                    verbiage.setGrayStroke(100);
                    verbiage.showTextAligned(PdfContentByte.ALIGN_CENTER, e, pageSize.getWidth()/2,pageSize.getHeight()/2,textAngle);
                    verbiage.endText();

                    underContent.beginText();
                    BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.WINANSI, BaseFont.EMBEDDED);
                    underContent.setFontAndSize(bf,6);
                    underContent.setRGBColorFill(255,0,0);
                    underContent.showTextAligned(Element.ALIGN_CENTER,a, 310, 775, 0);   // 300,746
                    underContent.showTextAligned(Element.ALIGN_CENTER,b, 310, 766, 0);  //  300,740
                    underContent.endText();
                }
                stamper.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (DocumentException e) {
                e.printStackTrace();
            }
            return baos.toByteArray();
        }




}
