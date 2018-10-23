package com.unagit.douuajobsevents.models

import com.unagit.douuajobsevents.helpers.ItemType
import java.lang.IllegalArgumentException

class DataInjector {
    companion object {

    private val items = listOf(
            Item(
                    "HR event «Требования современного IT рынка. Как искать и найти работу в IT», 25 октября, Одесса",
                    "https://dou.ua/calendar/23759/",
                    ItemType.EVENT,
                    "https://s.dou.ua/CACHE/images/img/events/700%D1%85450/9c59ac3883b39a74909457823f949800.png",
                    "&lt;p&gt;&lt;a href=\"https://dou.ua/calendar/23757/\" target=\"_blank\"&gt;&lt;img src=\"https://s.dou.ua/CACHE/images/img/events/%D0%93%D0%BB%D1%83%D0%B7%D0%B4%D0%BE%D0%B3%D0%B5%D1%80%D1%86%D1%8C-03/3f21738b350137d20cb59a8c3a7da9d9.png\" style=\"float: right; padding-left: 4px;\"&gt;&lt;/a&gt;&lt;strong&gt;Дата:&lt;/strong&gt; 1 листопада (четвер)&lt;br&gt;&lt;strong&gt;Час:&lt;/strong&gt; 18:00 — 21:00&lt;br&gt;&lt;strong&gt;Місце:&lt;/strong&gt; Дніпро, вулиця Шолом-Алейхема, 4/26, КДЦ «Менора»&lt;/p&gt;\n" +
                            "\n" +
                            "&lt;p&gt;Всім привіт!&lt;/p&gt;\n" +
                            "\n" +
                            "&lt;p&gt;Нас в SoftServe Dnipro вже 700 і з цієї нагоди ми запрошуємо вас на IT-квіз ГлуздоГерць!&lt;/p&gt;\n" +
                            "\n" +
                            "&lt;p&gt;ГлуздоГерць — це інтелектуально-розважальна гра, що відбувається у популярному форматі «Pub Quiz».&lt;/p&gt;\n" +
                            "\n" +
                            "&lt;p&gt;Ви будете мати можливість весело та атмосферно провести час разом із друзями, товаришами та колегами.&lt;/p&gt;\n" +
                            "\n" +
                            "&lt;p&gt;ГлуздоГерць — це простір для Вашого розвитку, можливість інвестувати у себе, адже мозок потребує тренувань. Ми знаємо, як зробити це весело.&lt;br&gt;Тому якщо Ви готові додати інтелектуального забарвлення у вже звичний відпочинок, цікаво та весело провести час у команді друзів, тоді ГлуздоГерць чекає на Вас.&lt;/p&gt;\n" +
                            "\n" +
                            "&lt;p&gt;Гра складається з 80 запитань, об’єднаних між собою у 8 турів.&lt;/p&gt;\n" +
                            "\n" +
                            "&lt;h4&gt;Тури поділяються на:&lt;/h4&gt;\n" +
                            "\n" +
                            "&lt;p style=\"margin-left: 20px;\"&gt;● музичні;&lt;br&gt;● текстові;&lt;br&gt;● графічні.&lt;/p&gt;\n" +
                            "\n" +
                            "&lt;p&gt;Час на обговорення питання всього-на-всього 30 секунд.&lt;/p&gt;\n" +
                            "\n" +
                            "&lt;h4&gt;Під час гри передбачено 3 перерви:&lt;/h4&gt;\n" +
                            "\n" +
                            "&lt;p style=\"margin-left: 20px;\"&gt;● після &lt;nobr&gt;3-го&lt;/nobr&gt; туру;&lt;br&gt;● після &lt;nobr&gt;6-го&lt;/nobr&gt; туру;&lt;br&gt;● перед оголошенням результатів.&lt;/p&gt;\n" +
                            "\n" +
                            "&lt;p&gt;У перерві між турами на вас чекає індивідуальний квіз за допомогою телеграм бота. Прийняти участь у цій «грі у грі» можуть усі учасники герцю. У вас буде унікальна можливість познайомитись з Gluzdo Bot.&lt;/p&gt;\n" +
                            "\n" +
                            "&lt;p&gt;Запрошуємо IT-спеціалістів різних напрямків об’єднуватися в команди та долучатися до гри! У складі однієї команди можуть бути від 4 до 7 гравців.&lt;/p&gt;\n" +
                            "\n" +
                            "&lt;p&gt;Зверніть увагу, що реєстрації приймаються від капітанів команд!&lt;/p&gt;\n" +
                            "\n" +
                            "&lt;p&gt;Участь безкоштовна.&lt;/p&gt;\n" +
                            "\n" +
                            "&lt;p&gt;&lt;a href=\"http://bit.ly/2NI7Knd\" target=\"_blank\"&gt;Реєстрація&lt;/a&gt;&lt;/p&gt;"
            ),
            Item(
                    "IT-квіз ГлуздоГерць, 1 листопада, Дніпро",
                    "https://dou.ua/calendar/23757/",
                    ItemType.EVENT,
                    "https://s.dou.ua/CACHE/images/img/events/%D0%93%D0%BB%D1%83%D0%B7%D0%B4%D0%BE%D0%B3%D0%B5%D1%80%D1%86%D1%8C-03/3f21738b350137d20cb59a8c3a7da9d9.png",
                    "&lt;p&gt;&lt;a href=\"https://dou.ua/calendar/23759/\" target=\"_blank\"&gt;&lt;img src=\"https://s.dou.ua/CACHE/images/img/events/700%D1%85450/9c59ac3883b39a74909457823f949800.png\" style=\"float: right; padding-left: 4px;\"&gt;&lt;/a&gt;&lt;strong&gt;Дата:&lt;/strong&gt; 25 октября (четверг)&lt;br&gt;&lt;strong&gt;Начало:&lt;/strong&gt; 18:30&lt;br&gt;&lt;strong&gt;Место:&lt;/strong&gt; Одесса, ул. Садовая 3, Компьютерная Академия «Шаг», &lt;nobr&gt;2-й&lt;/nobr&gt; этаж, Конференц-зал № 32&lt;/p&gt;\n" +
                            "\n" +
                            "&lt;p&gt;Ищете работу в сфере IT? Интересуют требования работодателей к IT-cпециалистам?&lt;/p&gt;\n" +
                            "\n" +
                            "&lt;p&gt;25 октября в 18:30 приглашаем на HR event «Требования современного IT рынка. Как искать и найти работу в IT».&lt;/p&gt;\n" +
                            "\n" +
                            "&lt;h4&gt;Программа мероприятия:&lt;/h4&gt;\n" +
                            "\n" +
                            "&lt;p&gt;— «Где искать работу или „точки входа“. Как вести себя на собеседовании с HR. Что ждет соискателя после собеседования с HR. Как грамотно составить резюме, основные ошибки» — расскажет Ольга Семенова (Olga Semenova), HR-director Одесского филиала Компьютерной Академии ШАГ. Опыт работы на должности HR director 8 лет.&lt;br&gt;— \"Как правильно пиарить себя в инфопространстве«— Максим Зубенко, руководитель отделов маркетинга и продаж Одесского филиала Академии ШАГ. &lt;br&gt;— «Как сократить путь от ученика к IT-специалисту. Немного о деньгах, сколько стоит IT-специалист» — Денис Степанов, коуч, тренер-психолог в Компьютерной Академии ШАГ. &lt;/p&gt;\n" +
                            "\n" +
                            "&lt;p&gt;&lt;strong&gt;Контакты:&lt;/strong&gt;&lt;br&gt;+38 (048) &lt;nobr class=\"phone\"&gt;728-66-60&lt;/nobr&gt;&lt;/p&gt;\n" +
                            "\n" +
                            "&lt;p&gt;Для участия &lt;a href=\"https://goo.gl/jeFYQ7\" target=\"_blank\"&gt;необходима регистрация&lt;/a&gt;.&lt;/p&gt;"
            )
    )

        fun getItems(): List<Item> {

            return items
        }

        fun getItemById(id: String): Item {
            items.forEach {
                if(it.guid == id) return it
            }
            throw IllegalArgumentException("Not able to find id in items.")
        }
    }
}