import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

public class CreatePdfFromImage {
    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.out.println("Usage: CreatePdfFromImage inputImage outputPdf");
            return;
        }

        String input = args[0];
        String output = args[1];

        PDDocument doc = new PDDocument();
        PDImageXObject pdImage = PDImageXObject.createFromFile(input, doc);
        PDPage page = new PDPage(new PDRectangle(pdImage.getWidth(), pdImage.getHeight()));
        doc.addPage(page);
        PDPageContentStream content = new PDPageContentStream(doc, page);
        content.drawImage(pdImage, 0, 0, pdImage.getWidth(), pdImage.getHeight());
        content.close();
        doc.save(output);
        doc.close();

        System.out.println("Saved PDF: " + output);
    }
}
