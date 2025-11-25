import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

// ==========================================
// 1. КЛАСИ ВИКЛЮЧЕНЬ (CUSTOM EXCEPTIONS)
// ==========================================

/**
 * Виключення, що виникає, коли обсяг товару перевищує вільне місце у фургоні.
 */
class VanCapacityExceededException extends Exception {
    public VanCapacityExceededException(String message) {
        super(message);
    }
}

/**
 * Виключення, що виникає, коли вартість товару перевищує залишок бюджету.
 */
class BudgetExceededException extends Exception {
    public BudgetExceededException(String message) {
        super(message);
    }
}

// ==========================================
// 2. ІЄРАРХІЯ ТОВАРІВ (COFFEE HIERARCHY)
// ==========================================

/**
 * Абстрактний базовий клас, що описує загальні характеристики кави.
 * Містить поля для ціни, ваги, об'єму та якості.
 */
abstract class Coffee {
    private String name;
    private double price;       // Ціна (наприклад, у гривнях)
    private double weight;      // Вага (кг)
    private double volume;      // Обсяг з упаковкою (літри або м^3)
    private int quality;        // Якість (рейтинг, наприклад від 1 до 100)

    /**
     * Конструктор для ініціалізації кави.
     *
     * @param name    Назва сорту або товару.
     * @param price   Ціна за одиницю товару.
     * @param weight  Фізична вага товару.
     * @param volume  Обсяг, який займає товар у фургоні.
     * @param quality Оцінка якості товару.
     */
    public Coffee(String name, double price, double weight, double volume, int quality) {
        this.name = name;
        this.price = price;
        this.weight = weight;
        this.volume = volume;
        this.quality = quality;
    }

    public String getName() { return name; }
    public double getPrice() { return price; }
    public double getWeight() { return weight; }
    public double getVolume() { return volume; }
    public int getQuality() { return quality; }

    /**
     * Розраховує співвідношення ціни до ваги.
     * Використовується для сортування згідно із завданням.
     *
     * @return Вартість одного кілограма даного товару.
     */
    public double getPriceToWeightRatio() {
        if (weight == 0) return 0;
        return price / weight;
    }

    @Override
    public String toString() {
        return String.format("ТОВАР: %-25s | Ціна: %7.2f | Вага: %5.2f | Об'єм: %5.2f | Якість: %3d | Ціна/Вага: %6.2f",
                name, price, weight, volume, quality, getPriceToWeightRatio());
    }
}

/**
 * Клас, що описує каву в зернах.
 * Зазвичай має більший об'єм упаковки (мішки).
 */
class CoffeeBeans extends Coffee {
    public CoffeeBeans(String name, double price, double weight, double volume, int quality) {
        super(name + " (Зерно)", price, weight, volume, quality);
    }
}

/**
 * Клас, що описує мелену каву.
 * Зазвичай вакуумна компактна упаковка.
 */
class GroundCoffee extends Coffee {
    public GroundCoffee(String name, double price, double weight, double volume, int quality) {
        super(name + " (Мелена)", price, weight, volume, quality);
    }
}

/**
 * Клас, що описує розчинну каву.
 * Може бути в банках або пакетиках.
 */
class InstantCoffee extends Coffee {
    private String packagingType; // Додаткове поле: тип упаковки

    public InstantCoffee(String name, double price, double weight, double volume, int quality, String packagingType) {
        super(name + " (Розчинна, " + packagingType + ")", price, weight, volume, quality);
        this.packagingType = packagingType;
    }
}

// ==========================================
// 3. КЛАС-КОНТЕЙНЕР (VAN)
// ==========================================

/**
 * Клас "Фургон", який накопичує товари, слідкує за бюджетом та місткістю,
 * а також виконує операції сортування та пошуку.
 */
class Van {
    private double maxVolume;       // Максимальна місткість фургона
    private double currentVolume;   // Поточна заповненість
    private double maxBudget;       // Максимальний бюджет на закупівлю
    private double currentCost;     // Потрачені кошти
    private List<Coffee> cargo;     // Список завантажених товарів

    /**
     * Створює порожній фургон із заданими обмеженнями.
     *
     * @param maxVolume Максимальний об'єм вантажу.
     * @param maxBudget Максимальна сума грошей.
     */
    public Van(double maxVolume, double maxBudget) {
        this.maxVolume = maxVolume;
        this.maxBudget = maxBudget;
        this.currentVolume = 0;
        this.currentCost = 0;
        this.cargo = new ArrayList<>();
    }

    /**
     * Намагається завантажити товар у фургон.
     * Перевіряє, чи не перевищено обсяг та бюджет.
     *
     * @param coffee Об'єкт товару (кави).
     * @throws VanCapacityExceededException Якщо товар не влазить фізично.
     * @throws BudgetExceededException      Якщо на товар не вистачає грошей.
     */
    public void loadCoffee(Coffee coffee) throws VanCapacityExceededException, BudgetExceededException {
        if (currentVolume + coffee.getVolume() > maxVolume) {
            throw new VanCapacityExceededException("ПОМИЛКА: Фургон переповнений! Не можна додати: " + coffee.getName());
        }
        if (currentCost + coffee.getPrice() > maxBudget) {
            throw new BudgetExceededException("ПОМИЛКА: Бюджет вичерпано! Не можна купити: " + coffee.getName());
        }

        cargo.add(coffee);
        currentVolume += coffee.getVolume();
        currentCost += coffee.getPrice();
        System.out.println("Успішно завантажено: " + coffee.getName());
    }

    /**
     * Сортує вантаж у фургоні на основі співвідношення ціни та ваги (Price / Weight).
     * Використовує стандартний компаратор.
     */
    public void sortByPriceToWeightRatio() {
        // Сортування за зростанням співвідношення
        Collections.sort(cargo, Comparator.comparingDouble(Coffee::getPriceToWeightRatio));
    }

    /**
     * Знаходить товари, які відповідають заданому діапазону якості.
     *
     * @param minQuality Мінімальна якість (включно).
     * @param maxQuality Максимальна якість (включно).
     * @return Список знайдених товарів.
     */
    public List<Coffee> findByQualityRange(int minQuality, int maxQuality) {
        List<Coffee> result = new ArrayList<>();
        for (Coffee c : cargo) {
            if (c.getQuality() >= minQuality && c.getQuality() <= maxQuality) {
                result.add(c);
            }
        }
        return result;
    }

    /**
     * Виводить у консоль поточний стан фургона та список товарів.
     */
    public void printCargoInfo() {
        System.out.println("\n------------------------------------------------------------------------------------------");
        System.out.printf("СТАН ФУРГОНА: Об'єм %.2f / %.2f | Бюджет %.2f / %.2f%n",
                currentVolume, maxVolume, currentCost, maxBudget);
        System.out.println("------------------------------------------------------------------------------------------");
        if (cargo.isEmpty()) {
            System.out.println("Фургон порожній.");
        } else {
            for (Coffee c : cargo) {
                System.out.println(c);
            }
        }
        System.out.println("------------------------------------------------------------------------------------------\n");
    }
}

// ==========================================
// 4. ГОЛОВНИЙ КЛАС (MAIN)
// ==========================================

/**
 * Головний клас для демонстрації роботи програми (Lab 5, Variant 11).
 */
public class Main {
    public static void main(String[] args) {
        // 1. Ініціалізація фургона (Об'єм: 50 од., Бюджет: 2000 грн)
        Van myVan = new Van(50.0, 2000.0);

        // 2. Створення об'єктів кави різних видів
        Coffee c1 = new CoffeeBeans("Arabica Gold", 400.0, 1.0, 5.0, 90);
        Coffee c2 = new GroundCoffee("Espresso Mix", 150.0, 0.25, 0.5, 75);
        Coffee c3 = new InstantCoffee("Jacobs", 200.0, 0.2, 0.4, 60, "Банка");
        Coffee c4 = new InstantCoffee("Nescafe 3in1", 5.0, 0.02, 0.05, 40, "Пакетик");
        Coffee c5 = new CoffeeBeans("Robusta Cheap", 100.0, 1.0, 5.0, 50);
        
        // Створимо дорогий і об'ємний товар для тесту виключень
        Coffee cExpensive = new CoffeeBeans("Kopi Luwak", 1500.0, 0.5, 2.0, 100);
        Coffee cHuge = new CoffeeBeans("Huge Bag", 50.0, 10.0, 45.0, 30);

        System.out.println("=== ЕТАП 1: ЗАВАНТАЖЕННЯ ===");
        try {
            myVan.loadCoffee(c1);
            myVan.loadCoffee(c2);
            myVan.loadCoffee(c3);
            myVan.loadCoffee(c4);
            myVan.loadCoffee(c5);
            
            // Спроба завантажити дорогий товар (може викликати BudgetExceeded, якщо бюджет < 2000 + поточні)
            // myVan.loadCoffee(cExpensive); 

            // Спроба завантажити завеликий товар (може викликати VanCapacityExceeded)
            // myVan.loadCoffee(cHuge);

        } catch (VanCapacityExceededException | BudgetExceededException e) {
            System.err.println(e.getMessage());
        }

        // Вивід вмісту до сортування
        System.out.println("Вміст до сортування:");
        myVan.printCargoInfo();

        // 3. Сортування
        System.out.println("=== ЕТАП 2: СОРТУВАННЯ (Ціна / Вага) ===");
        myVan.sortByPriceToWeightRatio();
        myVan.printCargoInfo();

        // 4. Пошук за якістю
        System.out.println("=== ЕТАП 3: ПОШУК (Якість від 70 до 100) ===");
        List<Coffee> searchResults = myVan.findByQualityRange(70, 100);
        
        if (searchResults.isEmpty()) {
            System.out.println("Товарів з такою якістю не знайдено.");
        } else {
            System.out.println("Знайдено товарів: " + searchResults.size());
            for (Coffee c : searchResults) {
                System.out.println(" -> " + c.getName() + " (Якість: " + c.getQuality() + ")");
            }
        }
    }
}