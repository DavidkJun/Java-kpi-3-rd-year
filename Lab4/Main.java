import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Основний клас для демонстрації Лабораторної роботи №4 (ООП).
 * Весь функціонал текстової моделі та обробки об'єднано у статичні внутрішні класи.
 * Виконує завдання сортування слів (з Lab2) на базі створеної об'єктної моделі.
 */
public class Main {
    
    // =========================================================================
    // 1. Елементарні класи (Літера та РозділовийЗнак)
    // =========================================================================
    
    /**
     * Клас, що представляє одну літеру в тексті.
     */
    private static class Letter {
        private final char value;

        /**
         * Конструктор для створення об'єкта Letter.
         * @param value Символьне значення літери.
         */
        public Letter(char value) { this.value = value; }

        /**
         * Повертає символьне значення літери.
         * @return Символ літери.
         */
        public char getValue() { return value; }

        @Override
        public String toString() { return String.valueOf(value); }
    }

    /**
     * Клас, що представляє розділовий знак в тексті.
     */
    private static class Punctuation {
        private final char value;

        /**
         * Конструктор для створення об'єкта Punctuation.
         * @param value Символьне значення розділового знака.
         */
        public Punctuation(char value) { this.value = value; }

        /**
         * Повертає символьне значення розділового знака.
         * @return Символ розділового знака.
         */
        public char getValue() { return value; }

        @Override
        public String toString() { return String.valueOf(value); }
    }

    // =========================================================================
    // 2. Клас Слово (Word)
    // =========================================================================
    
    /**
     * Клас, що представляє слово в тексті.
     * Складається зі списку об'єктів Letter (Композиція).
     */
    private static class Word {
        private final List<Letter> letters;

        /**
         * Конструктор для створення об'єкта Word з рядка.
         * @param wordString Вхідний рядок, що представляє слово.
         */
        public Word(String wordString) {
            this.letters = new ArrayList<>();
            for (char c : wordString.toCharArray()) {
                this.letters.add(new Letter(c));
            }
        }

        /**
         * Підраховує кількість входжень цільового символу (без урахування регістру).
         * @param targetChar Цільовий символ для підрахунку.
         * @return Кількість входжень цільового символу.
         */
        public int countTargetChar(char targetChar) {
            int count = 0;
            char lowerTarget = Character.toLowerCase(targetChar);
            for (Letter letter : letters) {
                if (Character.toLowerCase(letter.getValue()) == lowerTarget) {
                    count++;
                }
            }
            return count;
        }

        /**
         * Повертає рядкове представлення слова.
         */
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (Letter letter : letters) {
                sb.append(letter.getValue());
            }
            return sb.toString();
        }
    }

    // =========================================================================
    // 3. Клас Речення (Sentence)
    // =========================================================================
    
    /**
     * Клас, що представляє речення в тексті.
     * Складається зі списку слів та розділових знаків.
     */
    private static class Sentence {
        // List може містити об'єкти Word та Punctuation
        private final List<Object> parts; 

        /**
         * Конструктор для створення об'єкта Sentence.
         */
        public Sentence() {
            this.parts = new ArrayList<>();
        }

        /**
         * Додає частину до речення (слово або розділовий знак).
         * @param part Об'єкт Word або Punctuation.
         */
        public void addPart(Object part) {
            if (part instanceof Word || part instanceof Punctuation) {
                this.parts.add(part);
            } else {
                throw new IllegalArgumentException("Частина речення має бути Word або Punctuation.");
            }
        }

        /**
         * Повертає список всіх слів у реченні.
         * @return Список об'єктів Word.
         */
        public List<Word> getWords() {
            List<Word> words = new ArrayList<>();
            for (Object part : parts) {
                if (part instanceof Word) {
                    words.add((Word) part);
                }
            }
            return words;
        }

        /**
         * Повертає рядкове представлення речення, коректно оброблюючи пробіли.
         */
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            
            for (Object part : parts) {
                if (part instanceof Word) {
                    // Додаємо пробіл перед словом, якщо це не початок речення
                    if (sb.length() > 0 && !isPunctuation(sb.charAt(sb.length() - 1))) {
                        sb.append(" ");
                    }
                    sb.append(part.toString());
                } else if (part instanceof Punctuation) {
                    char puncChar = ((Punctuation) part).getValue();
                    
                    // Видаляємо пробіл перед пунктуацією, якщо він існує
                    if (sb.length() > 0 && sb.charAt(sb.length() - 1) == ' ') {
                        sb.deleteCharAt(sb.length() - 1);
                    }
                    sb.append(puncChar);
                }
            }
            // Це trim потрібно для випадку, якщо останній елемент був пробілом (хоча логіка це блокує)
            return sb.toString().trim();
        }
        
        /**
         * Допоміжна функція для перевірки, чи є символ пунктуацією, яка "прилипає" до попереднього слова.
         */
        private boolean isPunctuation(char c) {
            // Перевіряємо, чи символ є загальною пунктуацією, що вимагає прилягання
            return Pattern.matches("\\p{Punct}", String.valueOf(c));
        }
    }

    // =========================================================================
    // 4. Клас Текст (Text)
    // =========================================================================
    
    /**
     * Клас, що представляє текст.
     * Складається зі списку об'єктів Sentence (Композиція).
     */
    private static class Text {
        private final List<Sentence> sentences;

        /**
         * Конструктор для створення об'єкта Text.
         */
        public Text() {
            this.sentences = new ArrayList<>();
        }

        /**
         * Додає речення до тексту.
         * @param sentence Об'єкт Sentence.
         */
        public void addSentence(Sentence sentence) {
            this.sentences.add(sentence);
        }

        /**
         * Повертає список всіх речень у тексті.
         * @return Список об'єктів Sentence.
         */
        public List<Sentence> getSentences() {
            return sentences;
        }

        /**
         * Повертає рядкове представлення тексту, додаючи один пробіл між реченнями.
         */
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (Sentence sentence : sentences) {
                // Кожне речення відділяється одним пробілом
                sb.append(sentence.toString()).append(" ");
            }
            return sb.toString().trim();
        }
    }
    
    // =========================================================================
    // 5. Клас TextProcessor (Логіка обробки та парсингу)
    // =========================================================================

    /**
     * Клас, що містить логіку обробки тексту, включаючи парсинг та сортування.
     */
    private static class TextProcessor {

        /**
         * Виконує повний цикл обробки тексту: парсинг, сортування слів та виведення результатів.
         * @param inputText Вхідний рядок тексту.
         * @param targetChar Цільовий символ для сортування.
         * @return Об'єкт Text, що представляє оброблений текст.
         */
        public Text processText(String inputText, char targetChar) {
            if (inputText == null || inputText.isEmpty()) {
                throw new IllegalArgumentException("Вхідний текст не може бути порожнім.");
            }

            // 1. Очищення тексту від зайвих пробілів/табуляцій.
            String cleanedText = inputText.replaceAll("[\\s\t]+", " ").trim();

            // 2. Розбір тексту на об'єктну модель.
            Text text = parseText(cleanedText);

            // 3. Збір та сортування всіх слів тексту.
            List<Word> allWords = new ArrayList<>();
            for (Sentence sentence : text.getSentences()) {
                allWords.addAll(sentence.getWords());
            }

            // Сортування слів за зростанням кількості входжень цільового символу.
            allWords.sort(Comparator.comparingInt(word -> word.countTargetChar(targetChar)));

            // 4. Формування рядка результату сортування (вимога Lab2)
            System.out.println("\n--- Результат сортування (завдання Lab2) ---");
            StringBuilder result = new StringBuilder();
            for (Word word : allWords) {
                result.append(word.toString())
                      .append(" (")
                      .append(word.countTargetChar(targetChar))
                      .append(") ");
            }
            System.out.println(result.toString().trim());
            System.out.println("------------------------------------------");

            return text;
        }

        /**
         * Розбирає вхідний рядок тексту на об'єкти Text, Sentence, Word, Punctuation.
         * @param cleanedText Вхідний рядок тексту після очищення.
         * @return Об'єкт Text.
         */
        private Text parseText(String cleanedText) {
            Text text = new Text();
            
            // Регулярний вираз для розділення тексту на речення.
            String[] sentenceStrings = cleanedText.split("(?<=[.!?])\\s*");
            
            // Регулярний вираз для розділення речення на слова та розділові знаки.
            // [\u0400-\u04FF\u0450-\u045F']+: захоплює кириличні літери та апостроф (слова).
            // \p{Punct}: захоплює розділовий знак.
            Pattern partPattern = Pattern.compile("([\u0400-\u04FF\u0450-\u045F']+|\\p{Punct})");

            for (String sentenceString : sentenceStrings) {
                if (sentenceString.trim().isEmpty()) {
                    continue;
                }
                Sentence sentence = new Sentence();
                Matcher matcher = partPattern.matcher(sentenceString);
                
                while (matcher.find()) {
                    String part = matcher.group(1);
                    
                    if (isPunctuation(part)) {
                        // Створюємо об'єкт Punctuation
                        sentence.addPart(new Punctuation(part.charAt(0)));
                    } else if (!part.trim().isEmpty()) {
                        // Створюємо об'єкт Word
                        sentence.addPart(new Word(part));
                    }
                }
                
                if (!sentence.getWords().isEmpty()) {
                    text.addSentence(sentence);
                }
            }

            return text;
        }

        /**
         * Перевіряє, чи є рядок розділовим знаком.
         */
        private boolean isPunctuation(String part) {
            return part.length() == 1 && Pattern.matches("\\p{Punct}", part);
        }
    }
    
    // =========================================================================
    // 6. Виконавчий метод MAIN
    // =========================================================================

    public static void main(String[] args) {
        try {
            // Вхідні дані
            final String inputText = "Це перше речення. Друге речення має більше слів! Третє речення, " +
                                     "де є слово з багатьма 'р'. Речення з словами: річка, ромашка, робот.";
            final char targetChar = 'р';

            System.out.println("--- Вхідні дані ---");
            System.out.println("Вхідний текст:\n" + inputText);
            System.out.println("Цільовий символ: '" + targetChar + "'");
            System.out.println("-------------------\n");

            // Створення та виконання обробника тексту
            TextProcessor processor = new TextProcessor();
            Text processedText = processor.processText(inputText, targetChar);

            System.out.println("\n--- Об'єктна структура (Для перевірки Lab4) ---");
            System.out.println("Текст (відновлено з об'єктів):\n" + processedText.toString());
            System.out.println("Кількість речень: " + processedText.getSentences().size());
            // Додаткова перевірка структури
            processedText.getSentences().forEach(sentence -> 
                System.out.println("  Речення: \"" + sentence.toString() + "\" (" + sentence.getWords().size() + " слів)")
            );
            System.out.println("--------------------------");

        } catch (Exception e) {
            System.err.println("Сталася помилка: " + e.getMessage());
            e.printStackTrace();
        }
    }
}