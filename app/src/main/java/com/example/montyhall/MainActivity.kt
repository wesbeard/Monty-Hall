package com.example.montyhall

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {

    private lateinit var door0: ImageButton
    private lateinit var door1: ImageButton
    private lateinit var door2: ImageButton
    private lateinit var textBox: TextView
    private lateinit var retryButton: Button
    private lateinit var statsBox: TextView
    private var firstDoorClicked = false
    private var eliminatedDoor: ImageButton? = null
    private var winningDoor: ImageButton? = null
    private var gameOver = false

    private var gamesWon = 0
    private var gamesLost = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        door0 = findViewById(R.id.door0)
        door1 = findViewById(R.id.door1)
        door2 = findViewById(R.id.door2)
        textBox = findViewById(R.id.textBox)
        retryButton = findViewById(R.id.retry)
        retryButton.visibility = View.GONE
        retryButton.isClickable = false
        statsBox = findViewById(R.id.stats)
        statsBox.visibility = View.GONE

        when (Random().nextInt(2)) {
            0 -> winningDoor = door0
            1 -> winningDoor = door1
            2 -> winningDoor = door2
        }

        textBox.text = "Only one door is a winner, choose wisely"

        door0.setOnClickListener(View.OnClickListener {
            doorClicked(door0, door1, door2)
        })
        door1.setOnClickListener(View.OnClickListener {
            doorClicked(door1, door0, door2)
        })
        door2.setOnClickListener(View.OnClickListener {
            doorClicked(door2, door0, door1)
        })

        retryButton.setOnClickListener(View.OnClickListener {
            reset()
        })
    }

    fun doorClicked(clickedDoor: ImageButton, otherDoor1: ImageButton, otherDoor2: ImageButton) {
        if (!gameOver) {
            if (!firstDoorClicked) {
                firstDoorClicked = true

                clickedDoor.setImageResource(R.drawable.chosen_door)

                when (Random().nextInt(1)) {
                    0 -> {
                        if (winningDoor != otherDoor1) {
                            eliminatedDoor = otherDoor1
                            otherDoor1.setImageResource(R.drawable.x_door)
                        } else {
                            eliminatedDoor = otherDoor2
                            otherDoor2.setImageResource(R.drawable.x_door)
                        }
                    }
                    1 -> {
                        if (winningDoor != otherDoor2) {
                            eliminatedDoor = otherDoor2
                            otherDoor2.setImageResource(R.drawable.x_door)
                        } else {
                            eliminatedDoor = otherDoor1
                            otherDoor1.setImageResource(R.drawable.x_door)
                        }
                    }
                }

                textBox.text =
                    "One door has been eliminated, you can switch to another door or choose the same one again"

            } else if (clickedDoor == eliminatedDoor) {
                textBox.text = "You can't choose the eliminated door!"
            } else {
                if (clickedDoor == winningDoor) {
                    clickedDoor.setImageResource(R.drawable.trophy)
                    if (otherDoor1 != eliminatedDoor) {
                        otherDoor1.setImageResource(R.drawable.door)
                    }
                    else {
                        otherDoor2.setImageResource(R.drawable.door)
                    }
                    textBox.text = "Winner!"
                    gamesWon++
                    gameOver = true
                } else {
                    clickedDoor.setImageResource(R.drawable.donkey)
                    winningDoor?.setImageResource(R.drawable.door)
                    textBox.text = "Loser!"
                    gamesLost++
                    gameOver = true
                }
                promptRetry()
            }
        }
    }

    private fun promptRetry() {
        retryButton.visibility = View.VISIBLE
        retryButton.isClickable = true
        statsBox.visibility = View.VISIBLE
        var winPercentage: Double = (gamesWon.toDouble() / (gamesWon + gamesLost) * 100)
        var formatter = DecimalFormat("#.#")
        statsBox.text = "Games won: $gamesWon   Games lost: $gamesLost   Win percentage: ${formatter.format(winPercentage)}%"
    }

    private fun reset() {
        gameOver = false
        retryButton.visibility = View.GONE
        retryButton.isClickable = false
        door0.setImageResource(R.drawable.door)
        door1.setImageResource(R.drawable.door)
        door2.setImageResource(R.drawable.door)

        eliminatedDoor = null
        firstDoorClicked = false

        when (Random().nextInt(2)) {
            0 -> winningDoor = door0
            1 -> winningDoor = door1
            2 -> winningDoor = door2
        }

        textBox.text = "Only one door is a winner, choose wisely!"
    }
}