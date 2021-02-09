package com.zyrox.util;

import java.io.*;
import java.util.Calendar;
import java.util.GregorianCalendar;

public final class ErrorFile extends FileOutputStream {
    private static Calendar calendar = new GregorianCalendar();
    private final PrintStream errorStream;

    public ErrorFile(String errorsFolder, String name) throws FileNotFoundException {
        super(createPath(errorsFolder, name), true);
        this.errorStream = System.err;
    }

    private static String createPath(String errorsFolder, String fileName) {
        int month = calendar.get(2);
        int dayOfMonth = calendar.get(5);
        StringBuilder builder = new StringBuilder();
        builder.append(errorsFolder);
        if (!errorsFolder.endsWith(File.separator)) {
            builder.append(File.separator);
        }

        builder.append(fileName);
        builder.append("_");
        builder.append(month < 10 ? "0" + month : Integer.valueOf(month)).append('-');
        builder.append(dayOfMonth < 10 ? "0" + dayOfMonth : Integer.valueOf(dayOfMonth)).append('-');
        builder.append(calendar.get(1));
        builder.append(".txt");
        File errorFile = new File(builder.toString());
        if (!errorFile.exists() || !errorFile.getParentFile().exists()) {
            errorFile.getParentFile().mkdirs();

            try {
                FileOutputStream e = new FileOutputStream(errorFile);
                e.write(new byte[0]);
                e.close();
            } catch (FileNotFoundException var7) {
                var7.printStackTrace();
            } catch (IOException var8) {
                var8.printStackTrace();
            }
        }

        return builder.toString();
    }

    @Override
    public void write(int b) throws IOException {
        this.errorStream.write(b);
        super.write(b);
    }

    @Override
    public void write(byte[] b) throws IOException {
        this.errorStream.write(b);
        super.write(b);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        this.errorStream.write(b, off, len);
        super.write(b, off, len);
    }
}
