package com.example.jogodaforca

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.jogodaforca.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val palavras = listOf("Coxinhas", "Vendedor", "Deliciosa")
    private val palavraSelecionada = palavras.random().uppercase()
    private val palavraOculta = CharArray(palavraSelecionada.length) { '_' }

    private var tentativas = 6
    private val letrasAdivinhadas = mutableSetOf<Char>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        atualizarTela()

        binding.buttonAdivinhar.setOnClickListener {
            val entrada = binding.editEntrada.text.toString().uppercase()
            if (entrada.isNotEmpty() && entrada.length == 1) {
                val letra = entrada[0]
                processarLetra(letra)
            } else {
                binding.textStatus.text = "Por favor, insira apenas uma letra."
            }
            binding.editEntrada.text.clear()
        }
    }

    private fun processarLetra(letra: Char) {
        if (letrasAdivinhadas.contains(letra)) {
            binding.textStatus.text = "Você já tentou essa letra."
        } else {
            letrasAdivinhadas.add(letra)
            if (palavraSelecionada.contains(letra)) {
                revelarLetra(letra)
            } else {
                tentativas--
            }
            atualizarTela()
        }
    }

    private fun revelarLetra(letra: Char) {
        palavraSelecionada.forEachIndexed { index, c ->
            if (c == letra) {
                palavraOculta[index] = letra
            }
        }
    }

    private fun atualizarTela() {
        binding.textOculto.text = palavraOculta.joinToString(" ")
        binding.textTentativas.text = "Restam: $tentativas"
        binding.textLetrasTentadas.text = "Letras tentadas: ${letrasAdivinhadas.joinToString(", ")}"

        when {
            palavraOculta.none { it == '_' } -> {
                binding.textStatus.text = "Parabéns! A palavra era $palavraSelecionada."
                finalizarJogo()
            }
            tentativas <= 0 -> {
                binding.textStatus.text = "Você perdeu! A palavra era $palavraSelecionada."
                finalizarJogo()
            }
        }
    }

    private fun finalizarJogo() {
        binding.buttonAdivinhar.isEnabled = false
        binding.editEntrada.isEnabled = false
    }
}
