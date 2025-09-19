import java.util.*;

public class Lab2 {

   
   
    public static void main(String[] args) {
        try {
            final int STUDENT_ID = 12;
            final int C3 = STUDENT_ID % 3; 
            final int C17 = STUDENT_ID % 17; 
            
            String inputText = "Це перше речення. Друге речення має більше слів! Третє речення, " +
                             "де є слово з багатьма 'р'. Речення з словами: річка, ромашка, робот.";
            
            final char targetChar = 'р';
            
            System.out.println("Вхідний текст:");
            System.out.println(inputText);
            System.out.println("\nЦільовий символ: '" + targetChar + "'");
            
            StringBuilder textBuilder = new StringBuilder(inputText);
            
            StringBuilder result = sortWordsByCharCount(textBuilder, targetChar);
            
            System.out.println("\nРезультат (слова відсортовані за кількістю '" + targetChar + "'):");
            System.out.println(result.toString());
            
        } catch (Exception e) {
            System.err.println("Сталася помилка: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static StringBuilder sortWordsByCharCount(StringBuilder textBuilder, char targetChar) {
        if (textBuilder == null || textBuilder.length() == 0) {
            throw new IllegalArgumentException("Текст не може бути порожнім");
        }
        
        
        List<WordWithCount> wordsList = extractWordsWithCharCount(textBuilder, targetChar);
        
        
        wordsList.sort(Comparator.comparingInt(WordWithCount::getCharCount));
        
        
        StringBuilder result = new StringBuilder();
        for (WordWithCount word : wordsList) {
            result.append(word.getWord()).append(" (").append(word.getCharCount()).append(") ");
        }
        
        return result;
    }
    
    
    private static List<WordWithCount> extractWordsWithCharCount(StringBuilder textBuilder, char targetChar) {
        List<WordWithCount> wordsList = new ArrayList<>();
        StringBuilder currentWord = new StringBuilder();
        
        for (int i = 0; i < textBuilder.length(); i++) {
            char c = textBuilder.charAt(i);
            
            if (Character.isLetter(c) || c == '\'') {
                
                currentWord.append(c);
            } else {
                
                if (currentWord.length() > 0) {
                    String word = currentWord.toString();
                    int count = countCharOccurrences(word, targetChar);
                    wordsList.add(new WordWithCount(word, count));
                    currentWord.setLength(0); 
                }
            }
        }
        
        
        if (currentWord.length() > 0) {
            String word = currentWord.toString();
            int count = countCharOccurrences(word, targetChar);
            wordsList.add(new WordWithCount(word, count));
        }
        
        return wordsList;
    }
    
    
    private static int countCharOccurrences(String word, char targetChar) {
        int count = 0;
        char lowerTarget = Character.toLowerCase(targetChar);
        
        for (int i = 0; i < word.length(); i++) {
            if (Character.toLowerCase(word.charAt(i)) == lowerTarget) {
                count++;
            }
        }
        
        return count;
    }
    
   
    private static class WordWithCount {
        private final String word;
        private final int charCount;
        
        public WordWithCount(String word, int charCount) {
            this.word = word;
            this.charCount = charCount;
        }
        
        public String getWord() {
            return word;
        }
        
        public int getCharCount() {
            return charCount;
        }
    }
    
 
    private static StringBuilder processTextWithStringBuilder(StringBuilder textBuilder) {
        
        StringBuilder result = new StringBuilder();
        
        
        result.append("Оброблений текст: ");
        result.append(textBuilder);
        
        
        result.insert(0, "Початок: ");
        
        
        result.replace(0, 8, "СТАРТ: ");
        
        
        if (result.length() > 50) {
            result.delete(50, result.length());
            result.append("...");
        }
        
        return result;
    }
}