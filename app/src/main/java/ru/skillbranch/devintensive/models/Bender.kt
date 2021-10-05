package ru.skillbranch.devintensive.models

class Bender(var status: Status = Status.NORMAL, var question: Question = Question.NAME) {

    fun askQuestion() = when (question) {
        Question.NAME -> Question.NAME.question
        Question.PROFESSION -> Question.PROFESSION.question
        Question.MATERIAL -> Question.MATERIAL.question
        Question.BDAY -> Question.BDAY.question
        Question.SERIAL -> Question.SERIAL.question
        Question.IDLE -> Question.IDLE.question
    }

    private fun answerValidation(answer: String): String? {
        return when {
            question == Question.NAME && answer[0].isLowerCase() ->
                "Имя должно начинаться с заглавной буквы"

            question == Question.PROFESSION && !answer[0].isLowerCase() ->
                "Профессия должна начинаться со строчной буквы"

            question == Question.MATERIAL && answer.any { "$it".matches("[\\d]".toRegex()) } ->
                "Материал не должен содержать цифр"

            question == Question.BDAY && answer.any { "$it".matches("[a-zA-Z]".toRegex()) } ->
                "Год моего рождения должен содержать только цифры"

            question == Question.SERIAL && !answer.matches("[\\d]{7}".toRegex()) ->
                "Серийный номер содержит только цифры, и их 7"
            else -> null
        }
    }

    fun listenAnswer(answer: String): Pair<String, Triple<Int, Int, Int>> {
        val validation = answerValidation(answer)
        return when {
            question == Question.IDLE -> {
                "На этом все, вопросов больше нет" to status.color
            }
            question.answers.contains(answer.lowercase()) -> {
                if (validation == null) {
                    question = question.nextQuestion()
                    if (question == Question.IDLE && status != Status.NORMAL) {
                        status = Status.NORMAL
                        "Отлично - ты справился\n${question.question}" to status.color
                    } else {
                        "Отлично - ты справился\n${question.question}" to status.color
                    }

                } else {
                    "$validation\n${question.question}" to status.color
                }
            }
            else -> {
                return when {
                    validation != null -> "$validation\n${question.question}" to status.color
                    status == Status.CRITICAL -> {
                        status = Status.NORMAL
                        question = Question.NAME
                        "Это неправильный ответ. Давай все по новой!\n${question.question}" to status.color
                    }
                    else -> {
                        status = status.nextStatus()
                        "Это неправильный ответ\n${question.question}" to status.color
                    }
                }
            }
        }
    }

    enum class Status(val color: Triple<Int, Int, Int>) {
        NORMAL(Triple(255, 255, 255)),
        WARNING(Triple(255, 120, 0)),
        DANGER(Triple(255, 60, 60)),
        CRITICAL(Triple(255, 0, 0));

        fun nextStatus(): Status {
            return if (this.ordinal < values().lastIndex) {
                values()[this.ordinal + 1]
            } else {
                values()[0]
            }
        }
    }

    enum class Question(val question: String, val answers: List<String>) {
        NAME("Как меня зовут?", listOf("бендер", "bender")) {
            override fun nextQuestion(): Question = PROFESSION
        },
        PROFESSION("Назови мою профессию?", listOf("сгибальщик", "bender")) {
            override fun nextQuestion(): Question = MATERIAL
        },
        MATERIAL("Из чего я сделан?", listOf("метал", "дерево", "metal", "iron", "wood")) {
            override fun nextQuestion(): Question = BDAY
        },
        BDAY("Когда меня создали?", listOf("2993")) {
            override fun nextQuestion(): Question = SERIAL
        },
        SERIAL("Мой серийный номер?", listOf("2716057")) {
            override fun nextQuestion(): Question = IDLE
        },
        IDLE("На этом все, вопросов больше нет", listOf()) {
            override fun nextQuestion(): Question = IDLE
        };

        abstract fun nextQuestion(): Question
    }
}