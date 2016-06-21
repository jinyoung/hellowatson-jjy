package wasdev.sample.servlet;

import com.ibm.watson.developer_cloud.text_to_speech.v1.TextToSpeech;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class SimpleServlet
 */
@WebServlet("/SimpleServlet")
public class SimpleServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("audio/wav");

        String message = request.getParameter("message");
        
        TextToSpeech service = new TextToSpeech();
        service.setEndPoint("https://stream.watsonplatform.net/text-to-speech/api");
		service.setUsernameAndPassword("089a5648-260f-4f05-aae9-530701581d9d", "6EXLSjLFcR6z");

        OutputStream os = response.getOutputStream();

        InputStream is = service.synthesize(message !=null ? message : "This is watson", "audio/wav");

        try {
            copyStream(is, os);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            os.flush();
            os.close();

            is.close();
        }


    }

    static public void copyStream(InputStream sourceInputStream, OutputStream targetOutputStream) throws Exception {
        int length = 1024;
        byte[] bytes = new byte[length];
        int c;
        int total_bytes = 0;

        while ((c = sourceInputStream.read(bytes)) != -1) {
            total_bytes += c;
            targetOutputStream.write(bytes, 0, c);
        }

        if (sourceInputStream != null) try {
            sourceInputStream.close();
        } catch (Exception e) {
        }
        if (targetOutputStream != null) try {
            targetOutputStream.close();
        } catch (Exception e) {
        }
    }

}
