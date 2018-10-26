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
                    "<p style=\"margin-left: 20px;\">● <em><strong>«Arcserve: захист даних у&nbsp;гібридних середовищах»</strong>, Володимир Людвіновський, Архітектор інфраструктурних рішень, Oberig IT</em><br>● <em><strong>«Основні питання юридичної безпеки IT-проектів в&nbsp;Україні»</strong>, Ігор Биков, IT-юрист, Tsykhonya Lawyers</em><br>● <em><strong>«iSwan&nbsp;— хмарний ERP-BI-CRM сервіс для малого і&nbsp;середнього бізнесу»</strong>, Михайло Лебединський, Директор по&nbsp;IТ, Swan</em><br>● <em><strong>«UCloud. Приклади використання»</strong>, Данило Бєлов, Директор з&nbsp;розвитку, UCloud</em><br>● <em><strong>«Додатковi рiшення ESET для всебiчного захисту IТ-iнфраструктури»</strong>, Дмитро Федоренко, провiдний менеджер вiддiлу продажiв, ESET Україна</em><br>● <em><strong>«Автоматизація та&nbsp;оптимізація бізнес-процесів компанії за&nbsp;допомогою хмарних рішень»</strong>, Антон Скоков, керівник команди по&nbsp;впровадженню онлайн-сервісу електронного документообігу «Вчасно», ТОВ «Вчасно Сервіс»</em><br>● <em><strong>«Новiтнi технологiчнi розробки ESET для протидiї сучасним кібератакам»</strong>, Вячеслав Заріцький, провідний технічний спеціаліст, ESET Україна</em><br>● <em><strong>«Нова екосистема хмарних рішень De&nbsp;Novo»</strong>, Дмитро Бахмацький, директор з&nbsp;маркетингу, De&nbsp;Novo</em></p>"
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

        fun getItemInPosition(position: Int): Item {
            return items[position]
        }
    }
}