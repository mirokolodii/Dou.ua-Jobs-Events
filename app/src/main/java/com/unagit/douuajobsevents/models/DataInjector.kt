package com.unagit.douuajobsevents.models

import android.util.Log
import androidx.core.text.HtmlCompat
import com.unagit.douuajobsevents.helpers.ItemType
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.lang.IllegalArgumentException

class DataInjector {
    companion object {

    private val rawitems = listOf(
            Item(
                    "HR event «Требования современного IT рынка. Как искать и найти работу в IT», 25 октября, Одесса",
                    "https://dou.ua/calendar/23759/",
                    ItemType.EVENT,
                    "https://s.dou.ua/CACHE/images/img/events/700%D1%85450/9c59ac3883b39a74909457823f949800.png",
                    "&lt;p&gt;&lt;a href=\"https://dou.ua/calendar/23925/\" target=\"_blank\"&gt;&lt;img src=\"https://s.dou.ua/CACHE/images/img/events/Sitecore_logo_horizontal_with_tagline_RedBlack_A4/58aa526901033b5e87f4233e048be80e.png\" style=\"float: right; padding-left: 4px;\"&gt;&lt;/a&gt;&lt;strong&gt;Дата:&lt;/strong&gt; 14 ноября (среда)&lt;br&gt;&lt;strong&gt;Место:&lt;/strong&gt; Днепр&lt;/p&gt;\n" +
                            "\n" +
                            "&lt;p&gt;Компания Sitecore объявляет набор на бесплатный курс по .Net с возможностью дальнейшего трудоустройства на позицию Junior Software Engineer в компанию Sitecore. &lt;/p&gt;\n" +
                            "\n" +
                            "&lt;p&gt;&lt;strong&gt;Старт курса:&lt;/strong&gt; 26 ноября 2018 года &lt;br&gt;&lt;strong&gt;Длительность курса: &lt;/strong&gt;10 недель &lt;br&gt;&lt;strong&gt;Частота занятий:&lt;/strong&gt; 2 занятия в неделю по 2 часа в офисе Sitecore &lt;/p&gt;\n" +
                            "\n" +
                            "&lt;h4&gt;Для успешного прохождения курса нашим будущим студентам необходимо: &lt;/h4&gt;\n" +
                            "\n" +
                            "&lt;p&gt;— Знать принципы ООП; &lt;br&gt;— Иметь базовые знания C#, ASP.NET; &lt;br&gt;— Иметь базовое знания HTML, CSS и JavaScript; &lt;br&gt;— Владеть английским на уровне intermediate и выше. &lt;/p&gt;\n" +
                            "\n" +
                            "&lt;h4&gt;Во время курса мы уделим время следующим темам: &lt;/h4&gt;\n" +
                            "\n" +
                            "&lt;p&gt;— .Net and C# basics.&lt;br&gt;— Abstraction models, Events. &lt;br&gt;— Collections. &lt;br&gt;— Code execution workflow, exceptions, multithreading, Garbage Collection. &lt;br&gt;— Reflection. &lt;br&gt;— Streams. &lt;br&gt;— Web development basics. &lt;br&gt;— DNS and Network basics. &lt;br&gt;— IIS hosting specifics. &lt;br&gt;— Request execution workflow, ASP.NET life cycle. &lt;br&gt;— ASP.NET MVC basics. &lt;br&gt;— JavaScript and CSS basics. &lt;br&gt;— ASP.NET security model basics. &lt;/p&gt;\n" +
                            "\n" +
                            "&lt;h4&gt;По завершению курса студенты получат: &lt;/h4&gt;\n" +
                            "\n" +
                            "&lt;p&gt;— Знать принципы ООП; &lt;br&gt;— Иметь базовые знания C#, ASP.NET; &lt;br&gt;— Иметь базовое знания HTML, CSS и JavaScript; &lt;br&gt;— Владеть английским на уровне intermediate и выше. &lt;/p&gt;\n" +
                            "\n" +
                            "&lt;h4&gt;Во время курса мы уделим время следующим темам: &lt;/h4&gt;\n" +
                            "\n" +
                            "&lt;p&gt;— .Net and C# basics.&lt;br&gt;— Abstraction models, Events. &lt;br&gt;— Collections. &lt;br&gt;— Code execution workflow, exceptions, multithreading, Garbage Collection. &lt;br&gt;— Reflection. &lt;br&gt;— Streams. &lt;br&gt;— Web development basics. &lt;br&gt;— DNS and Network basics. &lt;br&gt;— IIS hosting specifics. &lt;br&gt;— Request execution workflow, ASP.NET life cycle. &lt;br&gt;— ASP.NET MVC basics. &lt;br&gt;— JavaScript and CSS basics. &lt;br&gt;— ASP.NET security model basics. &lt;/p&gt;\n" +
                            "\n" +
                            "&lt;h4&gt;По завершению курса студенты получат: &lt;/h4&gt;\n" +
                            "\n" +
                            "&lt;p&gt;— Востребованные на рынке знания в области .Net и Web разработки; &lt;br&gt;— Персональные рекомендации и программу для дальнейшего самообучения; &lt;br&gt;— Лучшие студенты будут приглашены на работу в компанию Sitecore. &lt;/p&gt;\n" +
                            "\n" +
                            "&lt;p&gt;Для того, чтобы подать заявку на курс, вам необходимо отправить свое резюме и мотивационное письмо на английском языке с темой письма «Sitecore .Net course» на &lt;a href=\"mailto:UA-HR@sitecore.net\"&gt;UA-HR@sitecore.net&lt;/a&gt;&lt;/p&gt;\n" +
                            "\n" +
                            "&lt;p&gt;После знакомства с вашим резюме мы назначим техническое он-лайн собеседование. &lt;/p&gt;\n" +
                            "\n" +
                            "&lt;p&gt;&lt;strong&gt;Дедлайн подачи заявок:&lt;/strong&gt; 14 ноября 2018 года. &lt;br&gt;Количество мест ограничено. &lt;/p&gt;\n" +
                            "\n" +
                            "&lt;p&gt;&lt;strong&gt;Sitecore Ukraine&lt;/strong&gt; — часть международной компании Sitecore A/S, которая разрабатывает собственную платформу упрампании находится в Дании, Копенгаген. Sitecore A/S существует с 2001 года. Офис в Днепре был основан в 2004 году. Работа компании охватывает полный цикл работы над собственным продуктом: замысел, создание и помощь.&lt;/p&gt;"
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
        private val items = ArrayList<Item>()


        fun getItems(): List<Item> {

            return items
        }

        fun printTestHtmlEl() {
            val tag = "testJsoup"
            rawitems.forEach {
                //                Log.d(tag, "Before-------------: \n ${it.description}")
                // Convert html codes into html tags
                val htmlStr = HtmlCompat.fromHtml(it.description, HtmlCompat.FROM_HTML_MODE_COMPACT)
//                Log.d(tag, "After fromHtml-------------: \n ${htmlStr.toString().length}")
//                Log.d(tag, "After toString-------------: \n ${htmlStr.toString()}")
                // Parse html
                val doc: Document = Jsoup.parseBodyFragment(htmlStr.toString())


                Log.d(tag, "Final--------------: \n $doc")
//                println("testJsoup: ${doc.body().html()}")

                val imgUrl = doc.body().selectFirst("p").selectFirst("img").attr("src")
                val itemDesc = doc.select("body > :gt(1)")
//                Log.d(tag, "Img Url--------------: \n $imgUrl")
                Log.d(tag, "P > 2--------------: \n $itemDesc \n")

            }

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