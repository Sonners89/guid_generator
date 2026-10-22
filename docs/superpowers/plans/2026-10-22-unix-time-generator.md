# Генератор unixTime — план реализации

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Добавить вторую кнопку `unixTime`, которая генерирует текущее время в микросекундах с эпохи Unix (16 цифр), копирует его в буфер обмена и показывает в модальном окне.

**Architecture:** Подход A — обобщение существующего метода показа сообщения. Все изменения в одном файле `IdentifierGenerator.java`: новый метод `generateUnixTime()`, параметризация `showCopiedMessage(String label, String value)`, вторая кнопка в `initUI()`.

**Tech Stack:** Java 21, Swing, `java.time.Instant`, Gradle.

**Spec:** `docs/superpowers/specs/2026-10-22-unix-time-generator-design.md`

> **ВАЖНО:** пользователь запретил коммитить без явного согласия. Шаг «Commit» выполняется **только после подтверждения пользователя**.

---

## Структура файлов

| Файл | Действие | Ответственность |
|------|----------|-----------------|
| `src/main/java/guidgenerator/IdentifierGenerator.java` | изменить | UI: две кнопки (`GUID`, `unixTime`), генерация GUID и unixTime, буфер, попап |

---

### Task 1: Добавить кнопку unixTime и обобщить показ сообщения

**Files:**
- Modify: `src/main/java/guidgenerator/IdentifierGenerator.java`

- [ ] **Step 1: Добавить импорт `java.time.Instant`**

В блок импортов добавить строку после `import java.util.UUID;`:

```java
import java.time.Instant;
```

Итоговый блок импортов должен выглядеть так:

```java
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.Instant;
import java.util.UUID;
```

- [ ] **Step 2: Заменить весь класс `IdentifierGenerator` на итоговое содержимое**

```java
package guidgenerator;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.Instant;
import java.util.UUID;

public class IdentifierGenerator extends JFrame {

    public IdentifierGenerator() {
        initUI();
    }

    private void initUI() {
        setTitle("Генератор идентификаторов");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(360, 120);
        setLayout(new FlowLayout(FlowLayout.CENTER));

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

        add(guidButton);
        add(unixTimeButton);

        setLocationRelativeTo(null);
    }

    private void generateNewGuid() {
        String guid = UUID.randomUUID().toString().toUpperCase();
        copyToClipboard(guid);
        showCopiedMessage("GUID", guid);
    }

    private void generateUnixTime() {
        Instant now = Instant.now();
        long micros = now.getEpochSecond() * 1_000_000L + now.getNano() / 1_000L;
        String value = String.valueOf(micros);
        copyToClipboard(value);
        showCopiedMessage("unixTime", value);
    }

    private void copyToClipboard(String text) {
        StringSelection stringSelection = new StringSelection(text);
        Toolkit.getDefaultToolkit().getSystemClipboard()
                .setContents(stringSelection, null);
    }

    private void showCopiedMessage(String label, String value) {
        JOptionPane.showMessageDialog(
                IdentifierGenerator.this,
                label + " скопирован в буфер обмена:\n\n" + value,
                "Готово",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}
```

- [ ] **Step 3: Собрать проект**

Run: `./gradlew.bat compileJava`
Expected: `BUILD SUCCESSFUL`.

- [ ] **Step 4: Ручная проверка**

Run: `./gradlew.bat run`
Expected: окно `360×120` с двумя кнопками `GUID` и `unixTime`.
- Клик по `GUID` → попап `Готово` с текстом `GUID скопирован в буфер обмена:` + GUID.
- Клик по `unixTime` → попап `Готово` с текстом `unixTime скопирован в буфер обмена:` + число из 16 цифр.
- Обе значения попадают в буфер обмена.
Закрыть окно.

- [ ] **Step 5: Commit (только с согласия пользователя)**

```bash
git add src/main/java/guidgenerator/IdentifierGenerator.java
git commit -m "GG: add unixTime generator button"
```

---

## Self-Review

**Spec coverage:**
- Вторая кнопка `unixTime`: Task 1 Step 2.
- Генерация микросекунд (`epochSecond * 1_000_000 + nano / 1_000`, без padding): Task 1 Step 2 (`generateUnixTime`).
- Копирование в буфер + модальный попап: Task 1 Step 2.
- Текст попапа `<label> скопирован в буфер обмена:\n\n<value>`: Task 1 Step 2 (`showCopiedMessage`).
- Порядок кнопок `GUID`, `unixTime`: Task 1 Step 2 (`add(guidButton); add(unixTimeButton);`).
- Размер окна `360×120`: Task 1 Step 2.
- GUID-путь обновлён на `showCopiedMessage("GUID", guid)`: Task 1 Step 2.
- Импорт `java.time.Instant`: Task 1 Step 1.
- Вне scope (padding, другие форматы, сервис-класс, обработка ошибок, тесты): задач нет.

**Placeholder scan:** плейсхолдеров нет; весь код и команды приведены полностью.

**Type consistency:** метод `generateUnixTime()` и подпись `showCopiedMessage(String label, String value)` совпадают между шагами и спецификацией; `copyToClipboard(String)` не меняется и переиспользуется обоими генераторами.
