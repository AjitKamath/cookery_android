package com.cookery.utils;

import android.util.Log;

import com.cookery.exceptions.CookeryException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import static com.cookery.utils.Constants.API_KEY_ANDROID;
import static com.cookery.utils.Constants.API_KEY_IDENTIFIER;
import static com.cookery.utils.Constants.APP_UNIQUE_ID;
import static com.cookery.utils.Constants.SERVER_TIMEOUT;

/**
 * Created by ajit on 7/1/17.
 */

public class MultipartUtility {
    private static final String CLASS_NAME = MultipartUtility.class.getName();


    private final String boundary;
    private static final String LINE_FEED = "\r\n";
    private HttpURLConnection httpConn;
    private String charset;
    private OutputStream outputStream;
    private PrintWriter writer;

    /**
     * This constructor initializes a new HTTP POST request with content type
     * is set to multipart/form-data
     *
     * @param requestURL
     * @param charset
     * @throws IOException
     */
    public MultipartUtility(String requestURL, String charset) throws CookeryException {
        try {
            this.charset = charset;

            // creates a unique boundary based on time stamp
            boundary = "===" + System.currentTimeMillis() + "===";

            URL url = new URL(requestURL);
            //Log.i(CLASS_NAME, "URL : " + requestURL.toString());
            httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setUseCaches(false);
            httpConn.setConnectTimeout(SERVER_TIMEOUT);
            httpConn.setDoOutput(true); // indicates POST method
            httpConn.setDoInput(true);
            httpConn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            //httpConn.setRequestProperty("User-Agent", "CodeJava Agent");
            httpConn.setRequestProperty(API_KEY_IDENTIFIER, API_KEY_ANDROID);
            httpConn.setRequestProperty(APP_UNIQUE_ID, Utility. getUniquePhoneId());
            outputStream = httpConn.getOutputStream();
            writer = new PrintWriter(new OutputStreamWriter(outputStream, charset), true);
        }
        catch (IOException ioe){
            throw new CookeryException(CookeryException.ErrorCode.NO_INTERNET, ioe);
        }
    }

    /**
     * Adds a form field to the request
     *
     * @param name  field name
     * @param value field value
     */
    public void addFormField(String name, String value) {
        writer.append("--" + boundary).append(LINE_FEED);
        writer.append("Content-Disposition: form-data; name=\"" + name + "\"").append(LINE_FEED);
        writer.append("Content-Type: text/plain; charset=" + charset).append(LINE_FEED);
        writer.append(LINE_FEED);
        writer.append(value).append(LINE_FEED);
        writer.flush();
    }

    /**
     * Adds a upload file section to the request
     *
     * @param fieldName  name attribute in <input type="file" name="..." />
     * @param uploadFile a File to be uploaded
     * @throws IOException
     */
    public void addFilePart(String fieldName, File uploadFile){
        FileInputStream inputStream = null;

        try {
            String fileName = uploadFile.getName();
            writer.append("--" + boundary).append(LINE_FEED);
            writer.append("Content-Disposition: form-data; name=\"" + fieldName + "\"; filename=\"" + fileName + "\"").append(LINE_FEED);
            writer.append("Content-Type: " + URLConnection.guessContentTypeFromName(fileName)).append(LINE_FEED);
            writer.append("Content-Transfer-Encoding: binary").append(LINE_FEED);
            writer.append(LINE_FEED);
            writer.flush();


            inputStream = new FileInputStream(uploadFile);
            byte[] buffer = new byte[4096];
            int bytesRead = -1;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            outputStream.flush();
            inputStream.close();

            writer.append(LINE_FEED);
            writer.flush();
        }
        catch (IOException ioe){
            throw new CookeryException(CookeryException.ErrorCode.NO_INTERNET, ioe);
        }
    }

    /**
     * Adds a header field to the request.
     *
     * @param name  - name of the header field
     * @param value - value of the header field
     */
    public void addHeaderField(String name, String value) {
        writer.append(name + ": " + value).append(LINE_FEED);
        writer.flush();
    }

    /**
     * Completes the request and receives response from the server.
     *
     * @return a list of Strings as response in case the server returned
     * status OK, otherwise an exception is thrown.
     * @throws IOException
     */
    public String finish() throws CookeryException {
        try {
            // checks server's status code first
            int status = httpConn.getResponseCode();

            if (status == HttpURLConnection.HTTP_OK) {
                StringBuffer response = new StringBuffer();

                BufferedReader reader = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                httpConn.disconnect();
                return String.valueOf(response);
            }
            else if (status == HttpURLConnection.HTTP_FORBIDDEN) {
                Log.e(CLASS_NAME, "Error ! Unauthorized service call. Possibly incorrect API key ?");
                throw new CookeryException(CookeryException.ErrorCode.ACCESS_DENIED);
            }
            else if (status == HttpURLConnection.HTTP_INTERNAL_ERROR) {
                Log.e(CLASS_NAME, "Error ! Possibly bad JSON ?");
                throw new CookeryException(CookeryException.ErrorCode.SOMETHING_WRONG);
            }
            else if (status == HttpURLConnection.HTTP_NOT_IMPLEMENTED) {
                Log.e(CLASS_NAME, "Error ! Possibly unimplemented function key ?");
                throw new CookeryException(CookeryException.ErrorCode.SOMETHING_WRONG);
            }
            else if (status == HttpURLConnection.HTTP_GATEWAY_TIMEOUT) {
                Log.e(CLASS_NAME, "Error ! Server took too long to respond");
                throw new CookeryException(CookeryException.ErrorCode.GATEWAY_TIMEOUT);
            }
            else{
                Log.e(CLASS_NAME, "Error ! No idea on what went wrong ! status code : "+status);
                throw new CookeryException(CookeryException.ErrorCode.SOMETHING_WRONG);
            }
        }
        catch (IOException ioe){
            throw new CookeryException(CookeryException.ErrorCode.NO_INTERNET);
        }
    }
}
