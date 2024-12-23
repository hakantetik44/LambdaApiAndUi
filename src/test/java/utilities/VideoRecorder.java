package utilities;

import io.qameta.allure.Attachment;
import org.monte.media.Format;
import org.monte.media.Registry;
import org.monte.media.math.Rational;
import org.monte.screenrecorder.ScreenRecorder;
import ws.schild.jave.Encoder;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.EncoderException;
import ws.schild.jave.encode.EncodingAttributes;
import ws.schild.jave.encode.VideoAttributes;
import org.openqa.selenium.WebDriver;

import java.awt.*;
import java.io.File;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.monte.media.FormatKeys.*;
import static org.monte.media.VideoFormatKeys.*;

public class VideoRecorder {
    private ScreenRecorder screenRecorder;
    private String currentVideoPath;
    private String currentMp4Path;
    private WebDriver driver;

    public VideoRecorder(WebDriver driver) {
        this.driver = driver;
        try {
            // Video kayıt klasörünü oluştur
            File folder = new File("target/videos");
            if (!folder.exists()) {
                folder.mkdirs();
            }

            // Chrome penceresinin bulunduğu ekranı bul
            org.openqa.selenium.Point browserPosition = driver.manage().window().getPosition();
            GraphicsDevice[] screens = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
            GraphicsDevice targetScreen = null;
            Rectangle browserRect = null;

            // Her ekranı kontrol et
            for (GraphicsDevice screen : screens) {
                Rectangle bounds = screen.getDefaultConfiguration().getBounds();
                if (bounds.contains(browserPosition.getX(), browserPosition.getY())) {
                    targetScreen = screen;
                    browserRect = bounds;
                    break;
                }
            }

            // Eğer ekran bulunamazsa varsayılan ekranı kullan
            if (targetScreen == null) {
                targetScreen = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
                browserRect = targetScreen.getDefaultConfiguration().getBounds();
            }

            System.out.println("Chrome'un bulunduğu ekran: " + browserRect);
            
            screenRecorder = new ScreenRecorder(
                    targetScreen.getDefaultConfiguration(),
                    browserRect,
                    new Format(MediaTypeKey, MediaType.FILE, MimeTypeKey, MIME_AVI),
                    new Format(MediaTypeKey, MediaType.VIDEO, EncodingKey, ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE,
                            CompressorNameKey, ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE,
                            DepthKey, 24, FrameRateKey, Rational.valueOf(30),
                            QualityKey, 1.0f,
                            KeyFrameIntervalKey, 30 * 60),
                    new Format(MediaTypeKey, MediaType.VIDEO, EncodingKey, "black",
                            FrameRateKey, Rational.valueOf(30)),
                    null,
                    new File(folder, "temp"));

            System.out.println("Video kaydedici oluşturuldu. Kayıt alanı: " + browserRect);
        } catch (Exception e) {
            System.err.println("Video kaydedici oluşturulamadı: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void startRecording(String testName) {
        try {
            if (screenRecorder != null) {
                screenRecorder.start();
                System.out.println("Video kaydı başlatıldı");
            }
        } catch (Exception e) {
            System.err.println("Video kaydı başlatılamadı: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void stopRecording(String testName) {
        try {
            if (screenRecorder != null) {
                screenRecorder.stop();
                System.out.println("Video kaydı durduruldu");

                if (!screenRecorder.getCreatedMovieFiles().isEmpty()) {
                    File sourceVideo = screenRecorder.getCreatedMovieFiles().get(0);
                    
                    // Video dosyasını yeniden adlandır
                    String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                    currentVideoPath = "target/videos/" + testName + "_" + timestamp + ".avi";
                    currentMp4Path = "target/videos/" + testName + "_" + timestamp + ".mp4";
                    
                    File targetVideo = new File(currentVideoPath);
                    File mp4Video = new File(currentMp4Path);
                    
                    if (sourceVideo.renameTo(targetVideo)) {
                        System.out.println("AVI video kaydedildi: " + currentVideoPath);
                        
                        // AVI'yi MP4'e dönüştür
                        convertToMp4(targetVideo, mp4Video);
                        
                        // MP4'ü Allure'a ekle
                        if (mp4Video.exists()) {
                            byte[] videoBytes = Files.readAllBytes(mp4Video.toPath());
                            attachVideo(videoBytes);
                            System.out.println("Video Allure'a eklendi: " + currentMp4Path);
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Video kaydı durdurulamadı: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void convertToMp4(File source, File target) throws EncoderException {
        try {
            // Video özelliklerini ayarla
            VideoAttributes videoAttributes = new VideoAttributes();
            videoAttributes.setCodec("h264");
            videoAttributes.setBitRate(2000000); // 2Mbps
            videoAttributes.setFrameRate(30);

            // Kodlama özelliklerini ayarla
            EncodingAttributes encodingAttributes = new EncodingAttributes();
            encodingAttributes.setVideoAttributes(videoAttributes);

            // Dönüştürme işlemini başlat
            Encoder encoder = new Encoder();
            encoder.encode(new MultimediaObject(source), target, encodingAttributes);
            
            System.out.println("Video MP4'e dönüştürüldü: " + target.getAbsolutePath());
        } catch (Exception e) {
            System.err.println("Video dönüştürme hatası: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Attachment(value = "Test Video", type = "video/mp4")
    private byte[] attachVideo(byte[] video) {
        return video;
    }

    public String getCurrentVideoPath() {
        return currentMp4Path;
    }
}
