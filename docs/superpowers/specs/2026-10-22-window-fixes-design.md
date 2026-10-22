# Дизайн: фиксированный размер окна и центрирование кнопок

Дата: 2026-10-22
Ветка: `feature/add_unixTimeGenerator`

## Цель

Две правки UI в окне "Генератор идентификаторов": убрать возможность развернуть
окно на весь экран и расположить кнопки точно по центру формы (по горизонтали и
по вертикали).

## Требования

1. Кнопка развёртывания окна на весь экран должна быть недоступна.
2. Кнопки `GUID` и `unixTime` должны находиться в центре формы — и по
   горизонтали, и по вертикали.

## Выбранный подход

Подход A — внешний `GridBagLayout` у `JFrame` + вложенная `JPanel` с
`FlowLayout(FlowLayout.CENTER)`.

Отклонённая альтернатива:

- **B** — `GridBagLayout` с двумя кнопками напрямую, каждая в своей ячейке с
  `anchor = CENTER`. Отклонено: лишний boilerplate `GridBagConstraints` на две
  кнопки без выгоды.

## Архитектура

Новых файлов нет. Изменяется только
`src/main/java/guidgenerator/IdentifierGenerator.java`.

Метод `generateNewGuid`, `generateUnixTime`, `copyToClipboard`,
`showCopiedMessage` — без изменений. Меняется только `initUI()`.

## Отключение разворачивания

`setResizable(false)` — на Windows делает кнопку максимизации недоступной и
фиксирует размер окна (`360×120`).

## Центрирование кнопок

- У `JFrame` устанавливается компоновка `GridBagLayout` (по умолчанию её
  единственный компонент центрируется в контейнере по обеим осям).
- Создаётся `JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER))`.
- В панель добавляются кнопки `GUID`, затем `unixTime`.
- Панель добавляется в окно: `add(buttonPanel);`.

Итоговый `initUI()`:

```java
private void initUI() {
    setTitle("Генератор идентификаторов");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(360, 120);
    setResizable(false);
    setLayout(new GridBagLayout());

    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

    JButton guidButton = new JButton("GUID");
    guidButton.setFont(new Font("Arial", Font.BOLD, 14));
    guidButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            generateNewGuid();
        }
    });

    JButton unixTimeButton = new JButton("unixTime");
    unixTimeButton.setFont(new Font("Arial", Font.BOLD, 14));
    unixTimeButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            generateUnixTime();
        }
    });

    buttonPanel.add(guidButton);
    buttonPanel.add(unixTimeButton);

    add(buttonPanel);

    setLocationRelativeTo(null);
}
```

## Обработка ошибок

Новых сценариев нет — поведение не меняется.

## Тестирование

Автоматические тесты не добавляются (логика в UI-классе). Ручная проверка:

1. `./gradlew run` → окно `360×120` с двумя кнопками по центру формы.
2. Кнопка максимизации недоступна; окно не растягивается.
3. Обе кнопки работают как раньше (буфер + попап).

## Вне scope

- Изменение размеров окна, заголовка, текста кнопок.
- Любые изменения логики генерации GUID/unixTime.
- Автотесты.
