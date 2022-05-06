package com.baojikouyu.teach.util;

public class FileTypeUtil {

    /**
     * Constructor
     */
    private FileTypeUtil() {
    }

    /**
     * @param value 文件的后缀名
     * @return 1 表示图片,2 表示文档,3 表示视频,4 表示种子,5 表示音乐,6 表示其它
     */
    public static Integer isFileType(String value) {
        Integer type = 6;// 其他
        // 图片
        FileType[] pics = {FileType.JPG, FileType.JPEG, FileType.PNG, FileType.GIF, FileType.TIFF, FileType.BMP, FileType.DWG, FileType.PSD};

        FileType[] docs = {FileType.RTF, FileType.XML, FileType.HTML, FileType.CSS, FileType.JS, FileType.EML, FileType.DBX, FileType.PST, FileType.XLS_DOC, FileType.XLSX_DOCX, FileType.VSD,
                FileType.MDB, FileType.WPS, FileType.WPD, FileType.EPS, FileType.PDF, FileType.QDF, FileType.PWL, FileType.ZIP, FileType.RAR, FileType.JSP, FileType.JAVA, FileType.CLASS,
                FileType.JAR, FileType.MF, FileType.EXE, FileType.CHM};

        FileType[] videos = {FileType.AVI, FileType.RAM, FileType.RM, FileType.MPG, FileType.MOV, FileType.ASF, FileType.MP4, FileType.FLV, FileType.MID};

        FileType[] tottents = {FileType.TORRENT};

        FileType[] audios = {FileType.WAV, FileType.MP3,FileType.MPEG};

        FileType[] others = {};

        // 图片
        for (FileType fileType : pics) {
            if (fileType.getValue().equals(value)) {
                type = 1;
            }
        }
        // 文档
        for (FileType fileType : docs) {
            if (fileType.getValue().equals(value)) {
                type = 2;
            }
        }
        // 视频
        for (FileType fileType : videos) {
            if (fileType.getValue().equals(value)) {
                type = 3;
            }
        }
        // 种子
        for (FileType fileType : tottents) {
            if (fileType.getValue().equals(value)) {
                type = 4;
            }
        }
        // 音乐
        for (FileType fileType : audios) {
            if (fileType.getValue().equals(value)) {
                type = 5;
            }
        }
        return type;
    }
}
