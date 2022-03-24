package com.baojikouyu.teach.util;

public enum FileType {
    /**
     * JEPG.
     */
    JPEG("JPEG"),

    JPG("JPG"),

    /**
     * PNG.
     */
    PNG("PNG"),

    /**
     * GIF.
     */
    GIF("GIF"),

    /**
     * TIFF.
     */
    TIFF("GIF"),

    /**
     * Windows Bitmap.
     */
    BMP("BMP"),

    /**
     * CAD.
     */
    DWG("DWG"),

    /**
     * Adobe Photoshop.
     */
    PSD("PSD"),

    /**
     * Rich Text Format.
     */
    RTF("RTF"),

    /**
     * XML.
     */
    XML("XML"),

    /**
     * HTML.
     */
    HTML("HTML"),
    /**
     * CSS.
     */
    CSS("CSS"),
    /**
     * JS.
     */
    JS("JS"),
    /**
     * Email [thorough only].
     */
    EML("EML"),

    /**
     * Outlook Express.
     */
    DBX("DBX"),

    /**
     * Outlook (pst).
     */
    PST("PST"),

    /**
     * MS Word/Excel.
     */
    XLS_DOC("XLS_DOC"), XLSX_DOCX("XLSX_DOCX"),
    /**
     * Visio
     */
    VSD("VSD"),
    /**
     * MS Access.
     */
    MDB("MDB"),
    /**
     * WPS文字wps、表格et、演示dps都是一样的
     */
    WPS("WPS"),
    /**
     * torrent
     */
    TORRENT("TORRENT"),
    /**
     * WordPerfect.
     */
    WPD("WPD"),

    /**
     * Postscript.
     */
    EPS("EPS"),

    /**
     * Adobe Acrobat.
     */
    PDF("PDF"),

    /**
     * Quicken.
     */
    QDF("QDF"),

    /**
     * Windows Password.
     */
    PWL("PWL"),

    /**
     * ZIP Archive.
     */
    ZIP("ZIP"),

    /**
     * RAR Archive.
     */
    RAR("RAR"),
    /**
     * JSP Archive.
     */
    JSP("JSP"),
    /**
     * JAVA Archive.
     */
    JAVA("JAVA"),
    /**
     * CLASS Archive.
     */
    CLASS("CLASS"),
    /**
     * JAR Archive.
     */
    JAR("JAR"),
    /**
     * MF Archive.
     */
    MF("MF"),
    /**
     * EXE Archive.
     */
    EXE("EXE"),
    /**
     * CHM Archive.
     */
    CHM("CHM"),
    /*
     * INI("235468697320636F6E66"), SQL("494E5345525420494E54"), BAT(
     * "406563686F206f66660D"), GZ("1F8B0800000000000000"), PROPERTIES(
     * "6C6F67346A2E726F6F74"), MXP(
     * "04000000010000001300"),
     */
    /**
     * Wave.
     */
    WAV("WAV"),

    /**
     * AVI.
     */
    AVI("AVI"),

    /**
     * Real Audio.
     */
    RAM("RAM"),

    /**
     * Real Media.
     */
    RM("RM"),

    /**
     * MPEG (mpg).
     */
    MPG("MPG"),

    MPEG("MPEG"),

    /**
     * Quicktime.
     */
    MOV("MOV"),

    /**
     * Windows Media.
     */
    ASF("ASF"),

    /**
     * MIDI.
     */
    MID("MID"),
    /**
     * MP4.
     */
    MP4("MP4"),
    /**
     * MP3.
     */
    MP3("MP3"),
    /**
     * FLV.
     */
    FLV("FLV");
    private String value = "";

    /**
     * Constructor.
     *
     * @param value
     */
    private FileType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


}
