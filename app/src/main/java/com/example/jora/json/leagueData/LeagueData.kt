package com.example.jora.json.leagueData

data class LeagueData(
    val leagues: List<League>
) {
    data class League(
        val name: String,
        val country: String,
        val teams: List<Team>
    )

    data class Team(
        val name: String,
        val city: String
    )

    fun printData() {
        println("Leagues:")
        for (league in leagues) {
            println("League: ${league.name} (${league.country})")
            println("Teams:")
            for (team in league.teams) {
                println(" - ${team.name} (${team.city})")
            }
        }
    }
}