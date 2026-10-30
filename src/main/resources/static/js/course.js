
const lessons = [
    {
        title: "1. Bevezetés",
        description:
            "A Java egy objektumorientált programozási nyelv. " +
            "Ebben a leckében megismerkedünk az alapokkal.",
        example:
`public class Main {
    public static void main(String[] args) {
        System.out.println("Hello, világ!");
    }
}`,
        task:
            "Készíts egy Java-programot, amely kiírja a konzolra: Hello, világ!",
        hint:
            "A program kiíratásához a System.out.println() utasítást használhatod. " +
            "A szöveget idézőjelek közé kell tenni. " +
            "Gondolj arra is, hogy a Java-programhoz szükséged lesz egy Main osztályra és egy main metódusra."
    },
    {
        title: "2. Változók és Típusok",
        description:
            "A változók olyan tárolók, amelyek adatokat mentenek el " +
            "a memóriában. Java-ban minden változónak típust kell adnunk.",
        example:
`int kor = 25;
System.out.println(kor);`,
        task:
            "Hozz létre egy szam nevű egész (int) változót, " +
            "adj neki értéket, és írasd ki a konzolra!",
        hint:
            "Hozz létre egy int típusú, szam nevű változót. " +
            "Az értékét az egyenlőségjellel adhatod meg. " +
            "A változó értékét a System.out.println(szam) utasítással írhatod ki."
    },
    {
        title: "3. Ciklusok",
        description:
            "A ciklusok segítségével egy utasítássorozatot többször is végrehajthatunk.",
        example:
`for (int i = 0; i < 5; i++) {
    System.out.println(i);
}`,
        task:
            "Írj egy ciklust, amely kiírja az 1-től 5-ig terjedő számokat!",
        hint:
            "Használj for ciklust. A ciklusváltozót állítsd 1-re, " +
            "majd minden ismétlés után növeld eggyel. " +
            "A ciklus addig fusson, amíg a változó kisebb vagy egyenlő 5-tel. " +
            "A ciklus törzsében írd ki a változó értékét."
    }
];

// Kezdetben az első lecke kivételével minden lecke zárolt.
const lockedLessons = new Set(
    lessons.map((lesson, index) => index).filter(index => index !== 0)
);

// Az első leckét jelenítjük meg kezdetben.
let currentLessonIndex = 0;

// HTML-elemek megkeresése.
const lessonTitle = document.getElementById("lesson-title");
const lessonDescription = document.getElementById("lesson-description");
const lessonExample = document.getElementById("lesson-example");
const lessonTask = document.getElementById("lesson-task");
const lessonList = document.getElementById("lesson-list");

const previousButton = document.getElementById("previous-lesson");
const nextButton = document.getElementById("next-lesson");

const validateButton = document.getElementById('validate-code');
const consoleOutput = document.getElementById('console-output');

// Az editor és a konzol visszaállítása alapállapotra.
function resetEditorAndConsole() {
    const editor = window.codeEditor;

    if (editor) {
        editor.setValue(`public class Main {
    public static void main(String[] args) {
        // Írd ide a kódod:

    }
}`);
    }

    if (consoleOutput) {
        consoleOutput.textContent = "A kódod ellenőrzésre vár.";
        consoleOutput.className = "text-gray-300 leading-relaxed";
    }
}

// Dinamikusan létrehozza a tartalomjegyzéket.
function renderLessonList() {
    lessonList.replaceChildren();

    lessons.forEach((lesson, index) => {
        const isLocked = lockedLessons.has(index);

        const listItem = document.createElement("li");
        const link = document.createElement("a");
        const icon = document.createElement("span");

        link.href = "#";
        link.dataset.lessonIndex = index;

        if (isLocked) {
            link.dataset.locked = "true";
            link.setAttribute("aria-disabled", "true");
        }

        link.className =
            "flex items-center gap-3 w-full p-2 rounded-lg transition";

        icon.textContent = isLocked ? "🔒" : "○";

        link.appendChild(icon);
        link.appendChild(document.createTextNode(lesson.title));
        listItem.appendChild(link);
        lessonList.appendChild(listItem);
    });
}

// Megjeleníti a kiválasztott leckét.
function showLesson(index) {
    if (index < 0 || index >= lessons.length) {
        return;
    }

    if (lockedLessons.has(index)) {
        return;
    }

    const previousLessonIndex = currentLessonIndex;

    currentLessonIndex = index;

    const lesson = lessons[index];

    // Visszaállítás, ha valóban másik leckére váltunk.
    if (previousLessonIndex !== currentLessonIndex) {
        resetEditorAndConsole();
    }

    lessonTitle.textContent = lesson.title;
    lessonDescription.textContent = lesson.description;
    lessonExample.textContent = lesson.example;
    lessonTask.textContent = lesson.task;

    // A tartalomjegyzék frissítése.
    lessonList.querySelectorAll("a[data-lesson-index]").forEach((link) => {
        const lessonIndex = Number(link.dataset.lessonIndex);
        const isActive = lessonIndex === currentLessonIndex;
        const isLocked = lockedLessons.has(lessonIndex);
        const icon = link.querySelector("span");

        link.className =
            "flex items-center gap-3 w-full p-2 rounded-lg transition";

        if (isActive) {
            link.classList.add(
                "bg-white",
                "text-blue-600",
                "font-medium",
                "border",
                "border-gray-200",
                "shadow-sm"
            );
        } else if (isLocked) {
            link.classList.add(
                "text-gray-400",
                "cursor-not-allowed"
            );
        } else {
            link.classList.add(
                "text-gray-600",
                "hover:bg-gray-200"
            );
        }

        if (icon) {
            icon.textContent = isLocked
                ? "🔒"
                : isActive
                    ? "▶️"
                    : lessonIndex < currentLessonIndex
                        ? "✅"
                        : "○";

            if (isLocked) {
                icon.className = "text-gray-400";
            }
        }
    });

    // Navigációs gombok állapota.
    previousButton.disabled = currentLessonIndex === 0;

    const nextIndex = currentLessonIndex + 1;

    nextButton.disabled =
        nextIndex >= lessons.length || lockedLessons.has(nextIndex);

    previousButton.classList.toggle(
        "opacity-50",
        previousButton.disabled
    );

    nextButton.classList.toggle(
        "opacity-50",
        nextButton.disabled
    );

    previousButton.classList.toggle(
        "cursor-not-allowed",
        previousButton.disabled
    );

    nextButton.classList.toggle(
        "cursor-not-allowed",
        nextButton.disabled
    );
}

// Kattintás a tartalomjegyzék egy leckéjére.
lessonList.addEventListener("click", (event) => {
    const link = event.target.closest("a[data-lesson-index]");

    if (!link) {
        return;
    }

    event.preventDefault();

    const index = Number(link.dataset.lessonIndex);

    showLesson(index);
});

// Előző lecke.
previousButton.addEventListener("click", () => {
    showLesson(currentLessonIndex - 1);
});

// Következő lecke.
nextButton.addEventListener("click", () => {
    showLesson(currentLessonIndex + 1);
});

// Kezdeti állapot beállítása.
renderLessonList();
showLesson(currentLessonIndex);

if (validateButton && consoleOutput) {
    validateButton.addEventListener('click', function () {
        const editor = window.codeEditor;

        if (!editor) {
            consoleOutput.textContent =
                'Hiba: a kódszerkesztő nem érhető el.';
            consoleOutput.className = 'text-red-400';
            return;
        }

        const code = editor.getValue();

        if (code.trim() === '') {
            consoleOutput.textContent =
                'Hiba: a kód nem lehet üres.';
            consoleOutput.className = 'text-red-400';
            return;
        }

        
        consoleOutput.textContent =
            'A kód ellenőrzése sikeres. ' +
            'Karakterek száma: ' + code.length + '.';

        consoleOutput.className = 'text-green-400';

        // A következő lecke feloldása, ha létezik.
        const nextIndex = currentLessonIndex + 1;

        if (nextIndex < lessons.length) {
            lockedLessons.delete(nextIndex);

            // A tartalomjegyzék és a navigáció frissítése.
            renderLessonList();
            showLesson(currentLessonIndex);
        }

    });
}

const hintButton = document.getElementById("hint-button");
const hintPopover = document.getElementById("hint-popover");
const closeHintButton = document.getElementById("close-hint-button");
const hintLessonTitle = document.getElementById("hint-lesson-title");
const hintContent = document.getElementById("hint-content");

// Megjeleníti vagy elrejti az útmutatót.
function setHintVisible(visible) {
    if (!hintButton || !hintPopover) {
        return;
    }

    hintButton.setAttribute("aria-expanded", String(visible));
    hintPopover.setAttribute("aria-hidden", String(!visible));

    hintPopover.classList.toggle("opacity-0", !visible);
    hintPopover.classList.toggle("invisible", !visible);
    hintPopover.classList.toggle("translate-y-2", !visible);

    hintPopover.classList.toggle("opacity-100", visible);
    hintPopover.classList.toggle("visible", visible);
    hintPopover.classList.toggle("translate-y-0", visible);

    hintPopover.classList.toggle("pointer-events-none", !visible);
}

// Megnyitja az aktuális lecke útmutatóját.
function openHint() {
    const lesson = lessons[currentLessonIndex];

    if (!lesson || !hintPopover) {
        return;
    }

    hintLessonTitle.textContent = lesson.title;
    hintContent.textContent =
        lesson.hint || "Ehhez a leckéhez még nem készült útmutató.";

    setHintVisible(true);
}

// Bezárja az útmutatót.
function closeHint() {
    setHintVisible(false);
}

if (hintButton && hintPopover && closeHintButton) {
    hintButton.addEventListener("click", () => {
        const isOpen =
            hintButton.getAttribute("aria-expanded") === "true";

        if (isOpen) {
            closeHint();
        } else {
            openHint();
        }
    });

    closeHintButton.addEventListener("click", closeHint);

    // Bezárás, ha a felhasználó az útmutatón kívülre kattint.
    document.addEventListener("click", (event) => {
        if (
            hintButton.getAttribute("aria-expanded") === "true" &&
            !hintPopover.contains(event.target) &&
            !hintButton.contains(event.target)
        ) {
            closeHint();
        }
    });

    // Bezárás az Escape billentyűvel.
    document.addEventListener("keydown", (event) => {
        if (
            event.key === "Escape" &&
            hintButton.getAttribute("aria-expanded") === "true"
        ) {
            closeHint();
            hintButton.focus();
        }
    });
}

