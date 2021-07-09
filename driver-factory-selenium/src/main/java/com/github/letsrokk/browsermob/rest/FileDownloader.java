package com.github.letsrokk.browsermob.rest;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class FileDownloader {

    public static final String CONTENT_TYPE_TEXT = "text/plain";

    public static final String CONTENT_TYPE_JSON = "application/json";

    public static final String CONTENT_TYPE_XML = "application/xml";
    public static final String CONTENT_TYPE_PDF = "application/pdf";

    public static final String CONTENT_TYPE_ZIP = "application/zip";
    public static final String CONTENT_TYPE_TAR_GZ = "application/x-gzip";

    public static final String CONTENT_TYPE_OCTETSTREAM = "application/octet-stream";

    private Charset fileCharset = Charset.forName("UTF-8");
    private Set<String> contentTypes = new HashSet<>();

    private FileDownloader addContentType(String contentType) {
        contentTypes.add(contentType);
        return this;
    }

    public FileDownloader withCharset(Charset charset) {
        this.fileCharset = charset;
        return this;
    }

    public FileDownloader withCharset(String charsetName) {
        this.fileCharset = Charset.forName(charsetName);
        return this;
    }

    public FileDownloader withContent(String contentType) {
        addContentType(contentType);
        return this;
    }

    public FileDownloader withContents(String... contentType) {
        for (int i = 0; i < contentType.length; i++) {
            addContentType(contentType[i]);
        }
        return this;
    }

    //["text/plain","application/zip"]

    private String filterTemplate = "" +
            "var contentTypes = [%s];" +
            "var contentType = contents.getContentType();" +
            "var matches = contentTypes.filter(function(stackValue){\n" +
            "  if(stackValue) {\n" +
            "    return contentType.startsWith(stackValue);\n" +
            "  }\n" +
            "});" +
            "if (matches.length > 0) {\n" +
            "    var textContent;" +
            "    if (contents.isText()) {\n" +
            "        var String = Java.type(\"java.lang.String\");\n" +
            "        var originalTextBytes = contents.getBinaryContents();\n" +
            "        var originalText = new String(originalTextBytes, \"%s\");\n" +
            "        textContent = Java.type(\"java.util.Base64\").getEncoder().encodeToString(originalText.getBytes(\"utf-8\"));\n" +
            "    } else {\n" +
            "        var binaryContents = contents.getBinaryContents();\n" +
            "        textContent = Java.type(\"java.util.Base64\").getEncoder().encodeToString(binaryContents);\n" +
            "    }" +
            "    " +
            "    response.headers().remove(\"Content-Type\");\n" +
            "    response.headers().remove(\"Content-Encoding\");\n" +
            "    response.headers().remove(\"Content-Disposition\");\n" +
            "    response.headers().add(\"Content-Type\", \"text/html;charset=utf-8\");\n" +
            "    var htmlContent = \"<html><head></head><body><div id='downloadedContent'>\" + textContent + \"</div></body></html>\";\n" +
            "    contents.setTextContents(htmlContent);\n" +
            "}\n";

    public RequestBody requestBody() {
        List<String> decorated = contentTypes.stream()
                .map(contentType -> "\"" + contentType + "\"")
                .collect(Collectors.toList());
        String filter = String.format(filterTemplate, StringUtils.join(decorated, ","), fileCharset.name());
        return RequestBody.create(MediaType.parse("text/plain"), filter);
    }
}
