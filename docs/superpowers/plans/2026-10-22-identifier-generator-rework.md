# Переработка "Генератор идентификаторов" — план реализации

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Переименовать приложение в "Генератор идентификаторов", оставить в окне только кнопку `GUID`, а по клику генерировать GUID, копировать его в буфер обмена и показывать в модальном окне.

**Architecture:** Подход A — минимальная переработка. Вся логика остаётся в одном UI-классе `IdentifierGenerator` (ex-`GUIDGenerator`). Поле `JTextArea` и всё, что его обслуживает, удаляется. Ре-брендинг затрагивает `build.gradle` и `settings.gradle`.

**Tech Stack:** Java 21, Swing, Gradle, launch4j, JUnit 5 (зависимости есть, тесты не добавляются).

**Spec:** `docs/superpowers/specs/2026-10-22-identifier-generator-rework-design.md`

> **ВАЖНО:** пользователь запретил коммитить без явного согласия. Шаги "Commit" в каждой задаче выполняются **только после подтверждения пользователя**.

---

## Структура файлов

| Файл | Действие | Ответственность |
|------|----------|-----------------|
| `src/main/java/guidgenerator/GUIDGenerator.java` | переименовать → `IdentifierGenerator.java` | UI: кнопка `GUID`, генерация, буфер, попап |
| `src/main/java/guidgenerator/Main.java` | изменить | точка входа, создаёт `IdentifierGenerator` |
| `build.gradle` | изменить | ре-брендинг имени артефактов и описания |
| `settings.gradle` | изменить | имя корневого проекта |

---

### Task 1: Переименовать класс и упростить UI

**Files:**
- Rename: `src/main/java/guidgenerator/GUIDGenerator.java` → `src/main/java/guidgenerator/IdentifierGenerator.java`
- Modify: `src/main/java/guidgenerator/Main.java`

- [ ] **Step 1: Переименовать файл класса**

```bash
git mv src/main/java/guidgenerator/GUIDGenerator.java src/main/java/guidgenerator/IdentifierGenerator.java
```

- [ ] **Step 2: Заменить содержимое `IdentifierGenerator.java`**

```java
package guidgenerator;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.UUID;

public class IdentifierGenerator extends JFrame {

    public IdentifierGenerator() {
        initUI();
    }

    private void initUI() {
        setTitle("Генератор идентификаторов");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 120);
        setLayout(new FlowLayout(FlowLayout.CENTER));

        JButton generateButton = new JButton("GUID");
        generateButton.setFont(new Font("Arial", Font.BOLD, 14));

        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateNewGuid();
            }
        });

        add(generateButton);

        setLocationRelativeTo(null);
    }

    private void generateNewGuid() {
        String guid = UUID.randomUUID().toString().toUpperCase();
        copyToClipboard(guid);
        showCopiedMessage(guid);
    }

    private void copyToClipboard(String text) {
        StringSelection stringSelection = new StringSelection(text);
        Toolkit.getDefaultToolkit().getSystemClipboard()
                .setContents(stringSelection, null);
    }

    private void showCopiedMessage(String guid) {
        JOptionPane.showMessageDialog(
                IdentifierGenerator.this,
                "GUID скопирован в буфер обмена:\n\n" + guid,
                "Готово",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}
```

- [ ] **Step 3: Обновить ссылку в `Main.java`**

Заменить строку:

```java
                GUIDGenerator app = new GUIDGenerator();
```

на:

```java
                IdentifierGenerator app = new IdentifierGenerator();
```

- [ ] **Step 4: Убедиться, что старых ссылок не осталось**

Run: `grep -rn "GUIDGenerator" src/`
Expected: пустой вывод (совпадений нет).

- [ ] **Step 5: Собрать проект**

Run: `./gradlew compileJava`
Expected: `BUILD SUCCESSFUL`.

- [ ] **Step 6: Ручная проверка UI**

Run: `./gradlew run`
Expected: окно с заголовком `Генератор идентификаторов`, единственной кнопкой `GUID`; до нажатия ничего не происходит; по клику GUID попадает в буфер и появляется попап `Готово` с этим GUID. Закрыть окно.

- [ ] **Step 7: Commit (только с согласия пользователя)**

```bash
git add src/main/java/guidgenerator/IdentifierGenerator.java src/main/java/guidgenerator/Main.java
git commit -m "GG: rename class to IdentifierGenerator and simplify UI"
```

---

### Task 2: Ре-брендинг сборки

**Files:**
- Modify: `build.gradle`
- Modify: `settings.gradle`

- [ ] **Step 1: Обновить `settings.gradle`**

Заменить:

```groovy
rootProject.name = 'guid-generator'
```

на:

```groovy
rootProject.name = 'identifier-generator'
```

- [ ] **Step 2: Обновить `description` в `build.gradle`**

Заменить:

```groovy
description = 'Генератор GUID'
```

на:

```groovy
description = 'Генератор идентификаторов'
```

- [ ] **Step 3: Обновить имя JAR и manifest**

В блоке `jar { }` заменить:

```groovy
                'Implementation-Title': 'GUID Generator',
```

на:

```groovy
                'Implementation-Title': 'Identifier Generator',
```

и заменить:

```groovy
    archiveBaseName = 'GUIDGenerator'
```

на:

```groovy
    archiveBaseName = 'IdentifierGenerator'
```

- [ ] **Step 4: Обновить блок `launch4j`**

Заменить:

```groovy
    outfile = 'GUIDGenerator.exe'
```

на:

```groovy
    outfile = 'IdentifierGenerator.exe'
```

и заменить:

```groovy
    jar = "${project.buildDir}/libs/GUIDGenerator.jar"
```

на:

```groovy
    jar = "${project.buildDir}/libs/IdentifierGenerator.jar"
```

и заменить:

```groovy
    productName = 'GUID Generator'
```

на:

```groovy
    productName = 'Генератор идентификаторов'
```

- [ ] **Step 5: Обновить задачу `prepareForLaunch4j`**

Заменить:

```groovy
        'GUIDGenerator.jar'
```

на:

```groovy
        'IdentifierGenerator.jar'
```

- [ ] **Step 6: Собрать JAR и проверить имя**

Run: `./gradlew clean jar`
Expected: `BUILD SUCCESSFUL`; создан файл `build/libs/IdentifierGenerator.jar`.

- [ ] **Step 7: Ручная проверка JAR**

Run: `java -jar build/libs/IdentifierGenerator.jar`
Expected: окно `Генератор идентификаторов` с кнопкой `GUID`; клик генерирует GUID и показывает попап. Закрыть окно.

- [ ] **Step 8: Commit (только с согласия пользователя)**

```bash
git add build.gradle settings.gradle
git commit -m "GG: rebrand build artifacts to IdentifierGenerator"
```

---

### Task 3: Финальная проверка `.exe` (опционально)

**Files:** нет изменений исходников.

- [ ] **Step 1: Собрать `.exe`**

Run: `./gradlew buildExe`
Expected: `BUILD SUCCESSFUL`; создан `build/launch4j/IdentifierGenerator.exe`.

- [ ] **Step 2: Проверить запуск `.exe`**

Запустить `build/launch4j/IdentifierGenerator.exe`.
Expected: появляется окно `Генератор идентификаторов` с кнопкой `GUID`.

---

## Self-Review

**Spec coverage:**
- Название → "Генератор идентификаторов": Task 1 Step 2, Task 2 Steps 2/5.
- Только кнопка `GUID`, поле убрано: Task 1 Step 2.
- Клик → GUID в буфер + попап с GUID: Task 1 Step 2 (`generateNewGuid`/`showCopiedMessage`).
- Без автогенерации при открытии: Task 1 Step 2 (нет `windowOpened`/`generateGuidSilently`).
- Переименование класса → `IdentifierGenerator`: Task 1 Steps 1–3.
- Переименование артефактов → `IdentifierGenerator`: Task 2 Steps 3–5.
- `settings.gradle` → `identifier-generator`: Task 2 Step 1.
- Не трогаем `icon.ico`, `companyName`, `copyright`, версию: план их не изменяет.
- Вне scope: Unix time, отдельный сервис, обработка ошибок буфера — задач нет.

**Placeholder scan:** плейсхолдеров нет; весь код и команды приведены полностью.

**Type consistency:** класс везде `IdentifierGenerator`; методы `generateNewGuid()`, `copyToClipboard(String)`, `showCopiedMessage(String)` совпадают между шагами и спецификацией.
