import java.io.*;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

public class Main {
    static ArrayList<MeteorologyInfo> metInfo = new ArrayList<MeteorologyInfo>();
    static ArrayList<Bank> bankInfo = new ArrayList<Bank>();
    static Scanner scanner = new Scanner(System.in);
    static String metFileName = "Meteorology.txt";
    static String bankFileName = "Bank.txt";
    public static class MeteorologyInfo
    {
        int day;
        int month;
        float airTemperature;
    }
    public static class Bank {
        String surname;
        String name;
        Date date;
        int suma;
    }

    public static void main(String[] args) {

        LoadFromFile();
        int index;
        do {
            System.out.print("1.Метеорологічні дані.\n2.Банк(додавання/вивід/вилучення/редагування даних.).\n3.Вихід.\nВведіть номер завдання:");
            index = scanner.nextByte();
            switch (index) {
                case 1: {
                    do {
                        System.out.print("1.Добавити день.\n2.Показати статистику.\n3.Назад в меню.\nВведіть номер завдання:");
                        index = scanner.nextByte();
                        switch (index) {
                            case 1:
                                AddMeteorologyInfo();
                                break;
                            case 2:
                                ShowInfo();
                                ;
                                break;
                        }
                    }
                    while (index != 3);
                    index = 0;
                }
                break;
                case 2: {
                    do {
                        System.out.print("1.Вивід.\n2.Додати.\n3.Редагувати.\n4.Шукати\n5.Видалити\n6.Назад в меню.\nВведіть номер завдання:");
                        index = scanner.nextByte();
                        switch (index) {
                            case 1:
                                ShowBank();
                                ;
                                break;

                            case 2:
                                AddBankInfo();
                                ;
                                break;
                            case 3:
                                EditBank();
                                ;
                                break;
                            case 4:
                                SearchBankInfo();
                                ;
                                break;
                            case 5:
                                RemoveBankInfo();
                                ;
                                break;

                        }

                    }
                    while (index != 6);
                    index = 0;
                }

                break;

            }

        } while (index != 3);
    }

    static void LoadFromFile() {

        metInfo.clear();
        bankInfo.clear();
        try {
            FileInputStream fis = new FileInputStream(metFileName);
            DataInputStream dis = new DataInputStream(fis);
            MeteorologyInfo temp;
            while (dis.available() > 0) {

                System.out.println();
                temp = new MeteorologyInfo();
                temp.day = dis.read();
                temp.month = dis.read();
                temp.airTemperature = dis.readFloat();
                metInfo.add(temp);
            }
            dis.close();


        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (IOException e) {

        }
        try {
            FileInputStream fisCloak = new FileInputStream(bankFileName);
            DataInputStream disBank = new DataInputStream(fisCloak);
            Bank tempBank;
            while (disBank.available() > 0) {
                tempBank = new Bank();
                tempBank.surname = disBank.readUTF();
                tempBank.name = disBank.readUTF();
                String dateString = disBank.readUTF();
                try {
                    tempBank.date = new SimpleDateFormat("dd/MM/y").parse(dateString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                tempBank.suma = disBank.read();

                // System.out.println("Surname: " + tempCloak.surname + " | Date: " + tempCloak.date + " | Duration: " + tempCloak.duration + " days | Index: " + tempCloak.inventoryIndex + " | Item: " + tempCloak.itemName);
                bankInfo.add(tempBank);

            }
            disBank.close();
            SortBank();
        } catch (FileNotFoundException e) {
            e.printStackTrace();


        } catch (IOException e) {

        }


    }

    static void AddBankInfo() {
        Bank temp = new Bank();
        System.out.print("Введіть прізвище: ");
        temp.surname = scanner.nextLine();
        temp.surname = scanner.nextLine();
        System.out.print("Введіть ім'я: ");
        temp.name = scanner.nextLine();
        byte day;
        byte month;
        do {
            System.out.print("Номер місяця: ");
            month = scanner.nextByte();
        } while (month > 12 || month < 1);
        Calendar calendar = new GregorianCalendar(LocalDate.now().getYear(), month - 1, 1);
        do {
            System.out.println("Максимум днів:" + calendar.getActualMaximum(calendar.DAY_OF_MONTH));
            System.out.print("Введіть день: ");
            day = scanner.nextByte();

        } while (day > calendar.getActualMaximum(calendar.DAY_OF_MONTH) || day < 1);
        calendar = new GregorianCalendar(LocalDate.now().getYear(), month - 1, day);
        temp.date = calendar.getTime();
        System.out.print("Введіть суму вкладу: ");
        temp.suma = scanner.nextByte();
        try {
            FileOutputStream fos = new FileOutputStream(bankFileName, true);
            DataOutputStream dos = new DataOutputStream(fos);
            dos.writeUTF(temp.surname);
            dos.writeUTF(temp.name);
            dos.writeUTF(new SimpleDateFormat("dd/MM/y", Locale.ENGLISH).format(calendar.getTime()));
            dos.write(temp.suma);
            dos.close();

            bankInfo.add(temp);
            fos.close();
            SortBank();
        } catch (IOException e) {
            System.out.println("Error");
        }


    }

    static void ShowBank() {
        int i = 1;
        if (bankInfo.isEmpty()) {
            System.out.println("Bank list is empty, add new data.");
        } else {
            System.out.println("Info list:");
        }
        for (Bank tmpBank : bankInfo) {

            System.out.println(i++ + ": Прізвище: " + tmpBank.surname +"| Ім'я: " +tmpBank.name+ " | Date: " + new SimpleDateFormat("dd/MM/y", Locale.ENGLISH).format(tmpBank.date.getTime()) + " | Сума: " + tmpBank.suma);
        }
        System.out.println();
    }

    static void EditBank() {
        ShowBank();
        byte index, lineIndex;
        do {
            System.out.print("Введіть індекс(" + "від 1 до " + bankInfo.size() + ") для редагування:");
            index = scanner.nextByte();
        } while (index < 1 || index > bankInfo.size());
        Bank tmp = new Bank();
        tmp = bankInfo.get(index - 1);
        do {
            do {
                System.out.print("Choose line to edit\n(1.Прізвище | 2.Ім'я | 3.Дату | 4.Суму|)\n(5.Назад в меню.\nВведіть номер завдання:");
                lineIndex = scanner.nextByte();
            } while (lineIndex < 1 || lineIndex > 6);



            switch (lineIndex) {
                case 1: {
                    System.out.print("Прізвище: ");
                    tmp.surname = scanner.nextLine();
                    tmp.surname = scanner.nextLine();
                }
                ;
                break;
                case 2: {
                    System.out.print("Ім'я: ");
                    tmp.name = scanner.nextLine();
                    tmp.name = scanner.nextLine();
                }
                ;
                break;
                case 3: {
                    byte day;
                    byte month;
                    do {
                        System.out.print("Введіть номер місяця: ");
                        month = scanner.nextByte();
                    } while (month > 12 || month < 1);
                    Calendar calendar = new GregorianCalendar(LocalDate.now().getYear(), month - 1, 1);
                    do {
                        System.out.println("Максимум днів:" + calendar.getActualMaximum(calendar.DAY_OF_MONTH));
                        System.out.print("Введіть день: ");
                        day = scanner.nextByte();

                    } while (day > calendar.getActualMaximum(calendar.DAY_OF_MONTH) || day < 1);
                    calendar = new GregorianCalendar(LocalDate.now().getYear(), month - 1, day);
                    tmp.date = calendar.getTime();
                }
                ;
                break;
                case 4: {
                        System.out.print("Сума: ");
                        tmp.suma = scanner.nextByte();

                }
                ;
                break;

            }
        } while (lineIndex != 5);
        bankInfo.set(index - 1, tmp);
        ReSaveBank();

    }

    static void SearchBankInfo() {
        System.out.println("Введіть дату вкладу:");
        byte day;
        byte month;
        do {
            System.out.print("Введіть номер місяця: ");
            month = scanner.nextByte();
        } while (month > 12 || month < 1);
        Calendar calendar = new GregorianCalendar(LocalDate.now().getYear(), month - 1, 1);
        do {
            System.out.println("Максимум днів:" + calendar.getActualMaximum(calendar.DAY_OF_MONTH));
            System.out.print("Введіть день: ");
            day = scanner.nextByte();

        } while (day > calendar.getActualMaximum(calendar.DAY_OF_MONTH) || day < 1);
        calendar = new GregorianCalendar(LocalDate.now().getYear(), month - 1, day);
        boolean flag = false;
        for (Bank tmbBank : bankInfo) {

            if (tmbBank.date.equals(calendar.getTime())) {
                flag = true;
                System.out.println("Прізвище: " + tmbBank.surname +" | Ім'я: " + tmbBank.name + " | Дата: " + new SimpleDateFormat("dd/MM/y", Locale.ENGLISH).format(tmbBank.date.getTime()) + " | Сума: " + tmbBank.suma);
            }
        }
        if (!flag) System.out.println("Немає");

    }

    static void  RemoveBankInfo() {
        ShowBank();
        byte index;
        do {
            System.out.println("1-" + bankInfo.size());
            System.out.print("Введіть індекс(" + "від 1 до " + bankInfo.size() + ") для вилучення:");
            index = scanner.nextByte();
        } while (index < 1 || index > bankInfo.size());
        bankInfo.remove(index - 1);
        System.out.println("-----Data removed-----");
        ReSaveBank();
    }

    static void SortBank() {
        if (!bankInfo.isEmpty()) {
            for (int i = 0; i < bankInfo.size() - 1; i++) {
                for (int j = 0; j < bankInfo.size() - i - 1; j++) {
                    if (bankInfo.get(j).suma < bankInfo.get(j + 1).suma) {
                        Bank temp = bankInfo.get(j);
                        bankInfo.set(j, bankInfo.get(j + 1));
                        bankInfo.set(j + 1, temp);
                    }
                }
            }
        }
    }

    static void ReSaveBank() {

        try {
            FileOutputStream fos = new FileOutputStream(bankFileName);
            DataOutputStream dos = new DataOutputStream(fos);
            for (Bank tmpCloak : bankInfo) {
                dos.writeUTF(tmpCloak.surname);
                dos.writeUTF(tmpCloak.name);
                dos.writeUTF(new SimpleDateFormat("dd/MM/y", Locale.ENGLISH).format(tmpCloak.date.getTime()));
                dos.write(tmpCloak.suma);
            }
            dos.close();
            fos.close();
            SortBank();
        } catch (IOException e) {
            System.out.println("Error");
        }
    }

    static void AddMeteorologyInfo() {
        byte day;
        byte month;
        float airTemper;
        do {
            System.out.print("Введіть номер місяці: ");
            month = scanner.nextByte();
        } while (month > 12 || month < 1);
        Calendar calendar = new GregorianCalendar(LocalDate.now().getYear(), month - 1, 1);
        do {
            System.out.println("Максимальний день:" + calendar.getActualMaximum(calendar.DAY_OF_MONTH));
            System.out.print("Введіть день: ");
            day = scanner.nextByte();

        } while (day > calendar.getActualMaximum(calendar.DAY_OF_MONTH) || day < 1);
        System.out.print("Введіть температуру: ");
        airTemper = scanner.nextFloat();


        try {
            FileOutputStream fos = new FileOutputStream(metFileName, true);
            DataOutputStream dos = new DataOutputStream(fos);
            dos.write(day);
            dos.write(month);
            dos.writeFloat(airTemper);
            dos.close();
            MeteorologyInfo temp = new MeteorologyInfo();
            temp.day = day;
            temp.month = month;
            temp.airTemperature = airTemper;
            metInfo.add(temp);
        } catch (IOException e) {
            System.out.println("Error");
        }
    }

    static void ShowInfo() {
        for (int i = 0; i < metInfo.size() - 1; i++) {
            for (int j = 0; j < metInfo.size() - i - 1; j++) {
                if (metInfo.get(j).airTemperature > metInfo.get(j + 1).airTemperature) {
                    MeteorologyInfo temp = metInfo.get(j);
                    metInfo.set(j, metInfo.get(j + 1));
                    metInfo.set(j + 1, temp);
                }
            }
        }
        MeteorologyInfo min,max;
        min = metInfo.get(0);
        max = metInfo.get(0);
        Calendar calendar;
       for(int i = 0; i < metInfo.size();i++)
       {
            if(metInfo.get(i).airTemperature > max.airTemperature) max = metInfo.get(i);
           if(metInfo.get(i).airTemperature < min.airTemperature) min = metInfo.get(i);
            calendar = new GregorianCalendar(LocalDate.now().getYear(), metInfo.get(i).month, metInfo.get(i).day);
           System.out.println(i + ": Дата: " + new SimpleDateFormat("dd/MM", Locale.ENGLISH).format(calendar.getTime()) + " | Температура: " + metInfo.get(i).airTemperature);
       }
        calendar = new GregorianCalendar(LocalDate.now().getYear(), max.month,max.day);
        System.out.println("День з найбільшою температурую: "+ ": Дата: " + new SimpleDateFormat("dd/MM", Locale.ENGLISH).format(calendar.getTime()) + " | Температура: " + max.airTemperature);
        calendar = new GregorianCalendar(LocalDate.now().getYear(), min.month,min.day);
        System.out.println("День з найменшою температурую: "+ ": Дата: " + new SimpleDateFormat("dd/MM", Locale.ENGLISH).format(calendar.getTime()) + " | Температура: " + min.airTemperature);
    }
}
