# Фиксированный размер окна и центрирование кнопок — план реализации

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Убрать кнопку развёртывания окна на весь экран и расположить кнопки точно по центру формы (по горизонтали и вертикали).

**Architecture:** Подход A — внешний `GridBagLayout` у `JFrame` центрирует вложенную `JPanel` с `FlowLayout(FlowLayout.CENTER)`, внутри которой лежат кнопки. Отключение максимизации — `setResizable(false)`.

**Tech Stack:** Java 21, Swing, Gradle.

**Spec:** `docs/superpowers/specs/2026-10-22-window-fixes-design.md`

> **ВАЖНО:** пользователь запретил коммитить без явного согласия. Шаг «Commit» выполняется **только после подтверждения пользователя**.

---

## Структура файлов

| Файл | Действие | Ответственность |
|------|----------|-----------------|
| `src/main/java/guidgenerator/IdentifierGenerator.java` | изменить (только `initUI()`) | UI: две кнопки по центру, фиксированный размер окна |

---

### Task 1: Отключить разворачивание и центрировать кнопки

**Files:**
- Modify: `src/main/java/guidgenerator/IdentifierGenerator.java` (метод `initUI()`)

- [ ] **Step 1: Заменить метод `initUI()`**

Заменить текущий метод `initUI()` на:

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

Методы `generateNewGuid`, `generateUnixTime`, `copyToClipboard`, `showCopiedMessage` не изменяются.

- [ ] **Step 2: Собрать проект**

Run: `./gradlew.bat compileJava`
Expected: `BUILD SUCCESSFUL`.

- [ ] **Step 3: Ручная проверка**

Run: `./gradlew.bat run`
Expected:
- окно `360×120`;
- кнопка максимизации (развернуть на весь экран) недоступна, окно не растягивается;
- кнопки `GUID` и `unixTime` расположены по центру формы (и по горизонтали, и по вертикали);
- обе кнопки работают: значение попадает в буфер и показывается попап.
Закрыть окно.

- [ ] **Step 4: Commit (только с согласия пользователя)**

```bash
git add src/main/java/guidgenerator/IdentifierGenerator.java
git commit -m "GG: fix window size and center buttons"
```

---

## Self-Review

**Spec coverage:**
- Убрана кнопка разворачивания: Task 1 Step 1 (`setResizable(false)`).
- Кнопки по центру (гориз. + верт.): Task 1 Step 1 (`GridBagLayout` + `JPanel(FlowLayout.CENTER)`).
- Размер `360×120`, обработчики, шрифты, методы генерации сохранены: Task 1 Step 1.
- Вне scope (размер/заголовок/тексты кнопок, логика генерации, автотесты): задач нет.

**Placeholder scan:** плейсхолдеров нет; весь код и команды приведены полностью.

**Type consistency:** `initUI()` остаётся единственным изменённым методом; имена кнопок `guidButton`/`unixTimeButton`, панели `buttonPanel` и вызовы `generateNewGuid()`/`generateUnixTime()` согласованы с существующим кодом.
