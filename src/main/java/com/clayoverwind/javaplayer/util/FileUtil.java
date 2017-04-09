package com.clayoverwind.javaplayer.util;

import java.io.*;

public class FileUtil {

    public static void createDir(final String absDirName) {
        final File dir = new File(absDirName);
        if (!dir.exists()) {
            dir.mkdir();
        }
    }

    public static String readFile(final String absFileName) {
        final File file = new File(absFileName);
        if (!file.exists()) {
            return null;
        }
        BufferedReader bufferedReader;
        final StringBuilder sb = new StringBuilder();
        try {
            bufferedReader = new BufferedReader(new FileReader(file.getAbsoluteFile()));
            String string;
            try {
                while ((string = bufferedReader.readLine()) != null) {
                    sb.append(string + "\n");
                }
            } finally {
                bufferedReader.close();
            }
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static boolean writeFile(final String absFileName, final String content) {
        BufferedWriter bufferedWriter;
        try {
            bufferedWriter = new BufferedWriter(new FileWriter(
                    new File(absFileName).getAbsoluteFile()));
            try {
                bufferedWriter.write(content);
            } finally {
                bufferedWriter.close();
            }
        } catch (final FileNotFoundException e) {
            e.printStackTrace();
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return true;
    }
    
    public static boolean writeFileUsingUTF8(final String absFileName, final String content) {
        BufferedWriter bufferedWriter;
        try {
        	bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(absFileName), "UTF-8"));
            try {
                bufferedWriter.write(content);
            } finally {
                bufferedWriter.close();
            }
        } catch (final FileNotFoundException e) {
            e.printStackTrace();
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return true;
    }
    public static void appendContentToFile(final String absFileName, final String content) {
        BufferedWriter bufferedWriter;
        try {
            bufferedWriter = new BufferedWriter(new FileWriter(
                    new File(absFileName).getAbsoluteFile(), true));
            try {
                bufferedWriter.write(content);
            } finally {
                bufferedWriter.close();
            }
        } catch (final FileNotFoundException e) {
            e.printStackTrace();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isFileExist(final String absFileName) {
        final File file = new File(absFileName);
        if (file.exists()) {
            return true;
        }
        return false;
    }

    public static boolean deleteFileIfExist(final String absFileName) {
        final File file = new File(absFileName);
        if (file.exists()) {
            return file.delete();
        }
        return true;
    }
    
    public static boolean readInputStreamAndWriteToFile(final String absFileName, InputStream is) {
    	FileOutputStream fos;
		try {
			fos = new FileOutputStream(absFileName);
			byte[] buffer = new byte[1024];
            int len = -1;
	    	while ((len = is.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
	    	}
	    	fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return true;
    }
    
    public static boolean readGzipInputStreamAndWriteToFile(final String absFileName, InputStream is) {
    	String content = StringUtil.gzipInputStreamToUTF8String(is);
    	return FileUtil.writeFile(absFileName, content);
    }
    
    public static String getCurDirPath() {
		File directory = new File("");//参数为空 
		String path = null;
		try {
			path = directory.getCanonicalPath();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return path;
    }
}
