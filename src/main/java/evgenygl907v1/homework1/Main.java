package evgenygl907v1.homework1;

public class Main {
    public static void main(String[] args) {
        MyHashMap<String, Integer> map = new MyHashMap<>();

        System.out.println("Инициализация:");
        System.out.println("Size: " + map.size());
        System.out.println("Capacity: " + map.capacity());

        System.out.println("\nДобавление элементов:");
        map.put("A", 100);
        map.put(null, 200);
        map.put("B", null);
        System.out.println("Size: " + map.size());
        System.out.println("Capacity: " + map.capacity());

        System.out.println("\nПолучение элементов:");
        System.out.println("get(A): " + map.get("A"));
        System.out.println("get(null): " + map.get(null));
        System.out.println("get(B): " + map.get("B"));
        System.out.println("get(C): " + map.get("C"));

        System.out.println("\nПерезапись:");
        map.put("A", 999);
        System.out.println("get(A): " + map.get("A"));
        System.out.println("Size: " + map.size());

        System.out.println("\nУдаление элементов:");
        System.out.println("get(A): " + map.get("A"));
        System.out.println("Size: " + map.size());
        map.remove("A");
        System.out.println("get(A): " + map.get("A"));
        System.out.println("Size: " + map.size());

        System.out.println("\nИзменение размера:");
        MyHashMap<Integer, String> resizeMap = new MyHashMap<>();
        for (int i = 1; i < 20; i++) {
            resizeMap.put(i, "value" + i);
            System.out.println("i= " + i + " Size: " + resizeMap.size() + " Capacity: " + resizeMap.capacity());
        }
    }
}
