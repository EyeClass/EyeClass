import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;
/*
 * CONTACT INFO
 * mesutpiskin.com
 * mesutpiskin@outlook.com
 *
 * Daha detayly bilgi i�in linklere g�z atynyz (SEE ALSO)
 * http://mesutpiskin.com/blog/opencv-ile-gui-uygulamalar.html
 * http://mesutpiskin.com/blog/opencv-matris-uzerinde-cizim-islemleri.html
 * http://mesutpiskin.com/blog/321.html
 *
 * Comment line language Turkish for translate
 * OpenCV version 3.1
 */
public class DetectFace {

    static JFrame frame;
    static JLabel lbl;
    static ImageIcon icon;

    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		/*Cascade Classifier i�in �?retilmi? veri k�mesi, opencv build/etc/haarcascades/ i�erisinde yer almaktadyr
		  Daha fazla bilgi i�in Haar Cascade synyflandyrycylaryna bakabilirsiniz.
		*/

        CascadeClassifier cascadeFaceClassifier = new CascadeClassifier(
                "D:\\Downloads\\OpenCvObjectDetection-master\\OpenCvObjectDetection-master\\data\\haarcascade files\\haarcascade_frontalface_default.xml");
        CascadeClassifier cascadeEyeClassifier = new CascadeClassifier(
                "D:\\Downloads\\OpenCvObjectDetection-master\\OpenCvObjectDetection-master\\data\\haarcascade files\\haarcascade_eye_tree_eyeglasses.xml");

        CascadeClassifier cascadeNoseClassifier = new CascadeClassifier(
                "D:\\Downloads\\OpenCvObjectDetection-master\\OpenCvObjectDetection-master\\data\\haarcascade files\\haarcascade_mcs_nose.xml");
        //CascadeClassifier cascadeMouthClassifier = new CascadeClassifier("OpenCV/haarcascades/haarcascade_mcs_mouth.xml"); haarcascade_mcs_mouth on 2.4.11
        //Varsayylan kamera aygytyny ba?lat
        VideoCapture videoDevice = new VideoCapture();
        videoDevice.open(0);
        if (videoDevice.isOpened()) {
            //Sonsuz bir d�ng� ile s�rekli olarak g�r�nt� aky?y sa?lanyr
            while (true) {
                Mat frameCapture = new Mat();
                videoDevice.read(frameCapture);

                //Yakalanan g�r�nt�y� �nce d�n�?t�r ve frame i�erisine y�kle
                MatOfRect faces = new MatOfRect();
                cascadeFaceClassifier.detectMultiScale(frameCapture, faces);
                //Yakalanan �er�eve varsa i�erisinde d�n ve y�z�n boyutlary �l��s�nde bir kare �iz
                for (Rect rect : faces.toArray()) {
                    //Sol �st k�?esine metin yaz
                    Imgproc.putText(frameCapture, "Face", new Point(rect.x,rect.y-5), 1, 2, new Scalar(0,0,255));
                    Imgproc.rectangle(frameCapture, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height),
                            new Scalar(0, 100, 0),3);
                }

                //G�zleri bul ve bulunan array i�erisinde d�nerek kare �iz
                MatOfRect eyes = new MatOfRect();
                cascadeEyeClassifier.detectMultiScale(frameCapture, eyes);
                for (Rect rect : eyes.toArray()) {
                    //Sol �st k�?esine metin yaz
                    Imgproc.putText(frameCapture, "Eye", new Point(rect.x,rect.y-5), 1, 2, new Scalar(0,0,255));
                    //Kare �iz
                    Imgproc.rectangle(frameCapture, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height),
                            new Scalar(200, 200, 100),2);
                }

                //Burunlary bul ve bulunan array i�erisinde d�nerek kare �iz
                MatOfRect nose = new MatOfRect();
                cascadeNoseClassifier.detectMultiScale(frameCapture, nose);
                for (Rect rect : nose.toArray()) {
                    //Sol �st k�?esine metin yaz
                    Imgproc.putText(frameCapture, "Nose", new Point(rect.x,rect.y-5), 1, 2, new Scalar(0,0,255));
                    //Kare �iz
                    Imgproc.rectangle(frameCapture, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height),
                            new Scalar(50, 255, 50),2);
                }

                //A?yz bul ve bulunan array i�erisinde d�nerek kare �iz
			   /*MatOfRect mouth = new MatOfRect();
				cascadeMouthClassifier.detectMultiScale(frameCapture, mouth);
				for (Rect rect : mouth.toArray()) {

					Imgproc.rectangle(frameCapture, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height),
							new Scalar(129, 90, 50),2);
				}

				*/

                //Resmi swing nesnesinde g�sterebilmek i�in �nce image haline �evir ve ekrana bas
                PushImage(ConvertMat2Image(frameCapture));
                System.out.println(String.format("%s y�z(FACES) %s g�z(EYE) %s burun(NOSE) detected.", faces.toArray().length,eyes.toArray().length,nose.toArray().length));
            }
        } else {
            System.out.println("Video aygytyna ba?lanylamady.");
            return;
        }
    }
    //Mat nesnesini image tipine d�n�?t�r
    private static BufferedImage ConvertMat2Image(Mat kameraVerisi) {


        MatOfByte byteMatVerisi = new MatOfByte();
        //Ara belle?e verilen formatta g�r�nt� kodlar
        Imgcodecs.imencode(".jpg", kameraVerisi, byteMatVerisi);
        //Mat nesnesinin toArray() metodu elemanlary byte dizisine �evirir
        byte[] byteArray = byteMatVerisi.toArray();
        BufferedImage goruntu = null;
        try {
            InputStream in = new ByteArrayInputStream(byteArray);
            goruntu = ImageIO.read(in);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return goruntu;
    }

    //Bir frame (�er�eve) olu?turur
    public static void PencereHazirla() {
        frame = new JFrame();
        frame.setLayout(new FlowLayout());
        frame.setSize(700, 600);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    //Resmi g�sterecek label olu?turur
    public static void PushImage(Image img2) {
        //Pencere olu?turulmamy? ise hazyrlanyr
        if (frame == null)
            PencereHazirla();
        //Daha �nceden bir g�r�nt� y�klenmi? ise yenisi i�in kaldyryr
        if (lbl != null)
            frame.remove(lbl);
        icon = new ImageIcon(img2);
        lbl = new JLabel();
        lbl.setIcon(icon);
        frame.add(lbl);
        //Frame nesnesini yeniler
        frame.revalidate();
    }
}