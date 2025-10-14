import java.io.*;
import java.util.*;


    class Hotel implements Comparable<Hotel> {
    private String city;//имя студента
    private String name;
    private int grade;


    public Hotel() {
    }

    public Hotel(String city, String name, int grade) {
        this.city = city;
        this.name = name;
        this.grade = grade;
    }

    public Hotel(Hotel a) {
        this.city = a.city;
        this.name = a.name;
        this.grade = a.grade;
    }

    @Override
    public int compareTo(Hotel other) {
        return this.city.compareTo(other.city);
    }

    @Override
    public String toString() {
        return String.format("Город: %-10s Отель: %-15s Звезды: %d", city, name, grade);
    }
    public String getCity() {
        return city;
    }

    public String getName() {
        return name;
    }

    public int getStars() {
        return grade;
    }}


class ListOfHotels {
        private List<Hotel> hotelList = new ArrayList<>();

        public static class CityComparator implements Comparator<Hotel> {
            @Override
            public int compare(Hotel h1, Hotel h2) {
                return h1.getCity().compareTo(h2.getCity());
            }
        }

        public static class StarsDescComparator implements Comparator<Hotel> {
            @Override
            public int compare(Hotel h1, Hotel h2) {
                return Integer.compare(h2.getStars(), h1.getStars()); // убывание
            }
        }

        public static class NameComparator implements Comparator<Hotel> {
            @Override
            public int compare(Hotel h1, Hotel h2) {
                return h1.getName().compareTo(h2.getName());
            }
        }


    public void readFromString(String line) {
            String[] parts = line.split(",");
            if (parts.length == 3) {
                try {
                    String city = parts[0].trim();
                    String name = parts[1].trim();
                    int stars = Integer.parseInt(parts[2].trim());
                    hotelList.add(new Hotel(city, name, stars));
                } catch (NumberFormatException e) {
                    System.out.println("Ошибка в формате данных: " + line);
                }
            } else {
                System.out.println("Неправильный формат строки: " + line);
            }
        }

        public void addHotel(Hotel hotel) {
            hotelList.add(hotel);
        }

        // Получить список всех отелей
        public List<Hotel> getHotelList() {
            return new ArrayList<>(hotelList);
        }

        public void sortByCityAndStars() {
            hotelList.sort(new CityComparator().thenComparing(new StarsDescComparator()));
        }


        public void displayAllHotels() {
            if (hotelList.isEmpty()) {
                System.out.println("Список отелей пуст.");
                return;
            }
            sortByCityAndStars();
            String currentCity = "";
            for (Hotel hotel : hotelList) {
                if (!hotel.getCity().equals(currentCity)) {
                    currentCity = hotel.getCity();
                }
                System.out.println(hotel);
            }
        }
        public List<Hotel> findHotelsByCity(String city) {
            List<Hotel> result = new ArrayList<>();
            for (Hotel hotel : hotelList) {
                if (hotel.getCity().equalsIgnoreCase(city)) {
                    result.add(hotel);
                }
            }
            result.sort(new StarsDescComparator());
            return result;
        }
        public List<String> findCitiesByHotelName(String hotelName) {
            List<String> cities = new ArrayList<>();
            for (Hotel hotel : hotelList) {
                if (hotel.getName().equalsIgnoreCase(hotelName)) {
                    cities.add(hotel.getCity());
                }
            }
            return cities;

    }


}

public class Main {
    public static void main(String[] args)
    {
            ListOfHotels hotels = new ListOfHotels();
            Scanner scanner = new Scanner(System.in);
            try {
                Scanner fileScanner = new Scanner(new File("hotel.txt"));
                while (fileScanner.hasNextLine()) {
                    String line = fileScanner.nextLine().trim();
                    if (!line.isEmpty()) {
                        hotels.readFromString(line);
                    }
                }
                fileScanner.close();
            } catch (FileNotFoundException e) {
                System.out.println("Файл 'hotel.txt' не найден");
            }

            // Главное меню
            while (true) {
                System.out.println("1 - Показать все отели");
                System.out.println("2 - Найти отели в городе");
                System.out.println("3 - Найти города по отелю");
                System.out.println("0 - Выход");

                System.out.print("Ваш выбор: ");

                String input = scanner.nextLine().trim();

                if (input.equals("1")) {
                    System.out.println("\nСПИСОК ВСЕХ ОТЕЛЕЙ:");
                    hotels.displayAllHotels();

                } else if (input.equals("2")) {
                    System.out.print("\nВведите город: ");
                    String city = scanner.nextLine().trim();
                    if (!city.isEmpty()) {
                        List<Hotel> cityHotels = hotels.findHotelsByCity(city);
                        if (!cityHotels.isEmpty()) {
                            System.out.println("\nОтели в городе " + city + ":");
                            for (Hotel hotel : cityHotels) {
                                System.out.println(hotel);
                            }
                        } else {
                            System.out.println("Отели не найдены");
                        }
                    }

                } else if (input.equals("3")) {
                    System.out.print("\nВведите название отеля: ");
                    String hotelName = scanner.nextLine().trim();
                    if (!hotelName.isEmpty()) {
                        List<String> cities = hotels.findCitiesByHotelName(hotelName);
                        if (!cities.isEmpty()) {
                            System.out.println("\nОтель '" + hotelName + "' есть в городах:");
                            for (String city : cities) {
                                System.out.println(city);
                            }
                        } else {
                            System.out.println("Отель не найден");
                        }
                    }

                } else if (input.equals("0")) {
                    System.out.println("Выход");
                    scanner.close();
                    break;

                } else {
                    System.out.println("Неверный выбор. Введите число от 0 до 3");
                }

                // Пауза перед следующим меню
                System.out.print("\nНажмите Enter чтобы продолжить...");
                scanner.nextLine();
            }
        }
    }
