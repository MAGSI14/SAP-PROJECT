package model;
public class TextMatchUtil {
    public static String normalize(String s) {
        if (s == null) return "";
        s = s.toLowerCase();

        s = s.replaceAll("[^\\p{IsAlphabetic}\\p{IsDigit} ]+", " ");
        s = s.replaceAll("\\s+", " ").trim();
        return s;
    }
    public static String latToBg(String s) {
        if (s == null) return "";
        String text = s.toLowerCase();
        text = text.replace("sht", "щ");
        text = text.replace("zh", "ж");
        text = text.replace("ch", "ч");
        text = text.replace("sh", "ш");
        text = text.replace("yu", "ю");
        text = text.replace("ya", "я");
        text = text.replace("ts", "ц");
        text = text.replace("you", "ю");
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            switch (ch) {
                case 'a': out.append("а"); break;
                case 'b': out.append("б"); break;
                case 'v': out.append("в"); break;
                case 'g': out.append("г"); break;
                case 'd': out.append("д"); break;
                case 'e': out.append("е"); break;
                case 'z': out.append("з"); break;
                case 'i': out.append("и"); break;
                case 'y': out.append("й"); break;
                case 'k': out.append("к"); break;
                case 'l': out.append("л"); break;
                case 'm': out.append("м"); break;
                case 'n': out.append("н"); break;
                case 'o': out.append("о"); break;
                case 'p': out.append("п"); break;
                case 'r': out.append("р"); break;
                case 's': out.append("с"); break;
                case 't': out.append("т"); break;
                case 'u': out.append("у"); break;
                case 'f': out.append("ф"); break;
                case 'h': out.append("х"); break;
                case 'c': out.append("ц"); break;
                default: out.append(ch);
            }
        }
        return out.toString();
    }
    public static String bgToLat(String s) {
        if (s == null) return "";
        StringBuilder out = new StringBuilder();
        String text = s.toLowerCase();

        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            switch (ch) {
                case 'а': out.append("a"); break;
                case 'б': out.append("b"); break;
                case 'в': out.append("v"); break;
                case 'г': out.append("g"); break;
                case 'д': out.append("d"); break;
                case 'е': out.append("e"); break;
                case 'ж': out.append("zh"); break;
                case 'з': out.append("z"); break;
                case 'и': out.append("i"); break;
                case 'й': out.append("y"); break;
                case 'к': out.append("k"); break;
                case 'л': out.append("l"); break;
                case 'м': out.append("m"); break;
                case 'н': out.append("n"); break;
                case 'о': out.append("o"); break;
                case 'п': out.append("p"); break;
                case 'р': out.append("r"); break;
                case 'с': out.append("s"); break;
                case 'т': out.append("t"); break;
                case 'у': out.append("u"); break;
                case 'ф': out.append("f"); break;
                case 'х': out.append("h"); break;
                case 'ц': out.append("ts"); break;
                case 'ч': out.append("ch"); break;
                case 'ш': out.append("sh"); break;
                case 'щ': out.append("sht"); break;
                case 'ъ': out.append("a"); break;
                case 'ю': out.append("yu"); break;
                case 'я': out.append("ya"); break;
                case 'ь': break;
                default: out.append(ch);
            }
        }
        return out.toString();
    }
    public static boolean equalsCrossScript(String input, String target) {
        String normalizedInput = normalize(input);
        String normalizedOutput = normalize(target);

        if (normalizedInput.equals(normalizedOutput)) {
            return true;
        }
        if (bgToLat(normalizedInput).equals(bgToLat(normalizedOutput))) {
            return true;
        }
        if (latToBg(normalizedInput).equals(latToBg(normalizedOutput))) {
            return true;
        }
        return false;
    }
    public static boolean containsCrossScript(String input, String target) {
        String normalizedInput = normalize(input);
        String normalizedOutput = normalize(target);
        if (normalizedInput.isBlank() || normalizedOutput.isBlank()){
            return false;
        }
        String inputWordLat = normalize(bgToLat(normalizedInput));
        String inputWordBg  = normalize(latToBg(normalizedInput));
        String targetWordsLat = normalize(bgToLat(normalizedOutput));
        String targetWordsBg  = normalize(latToBg(normalizedOutput));

        for (String word : targetWordsLat.split("\\s+")) {
            if (word.equals(inputWordLat)){
                return true;
            }
        }
        for (String word : targetWordsBg.split("\\s+")) {
            if (word.equals(inputWordBg)) {
                return true;
            }
        }
        return false;
    }

}
