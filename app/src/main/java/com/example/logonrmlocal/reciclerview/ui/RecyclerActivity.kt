package com.example.logonrmlocal.reciclerview.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.LinearLayout
import android.widget.Toast
import com.example.logonrmlocal.reciclerview.R
import com.example.logonrmlocal.reciclerview.api.getPokemonAPI
import com.example.logonrmlocal.reciclerview.model.Pokemon
import com.example.logonrmlocal.reciclerview.model.PokemonResponse
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_recycler.*

class RecyclerActivity : AppCompatActivity() {

    private var disposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler)

        carregarDados()

    }

    private fun exibeNaLista(pokemons: List<Pokemon>){
        rvPokemons.adapter = RecyclerAdapter(this,pokemons,{
            Toast.makeText(this,it.nome,Toast.LENGTH_LONG).show()
        })
        rvPokemons.layoutManager = LinearLayoutManager(this)
    }

    private fun exibeErro(t: String) {
        Toast.makeText(this,t,Toast.LENGTH_LONG).show()
    }

    private fun carregarDados(){
        getPokemonAPI()
                .buscar(150)
                .subscribeOn(Schedulers.io()) //informa que vai trabalhar com io
                .observeOn(AndroidSchedulers.mainThread()) //manipular principal thread
                .subscribe(object: Observer<PokemonResponse>{

                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable) {
                        disposable = d
                    }

                    override fun onNext(t: PokemonResponse) {
                        exibeNaLista(t.pokemons)
                    }

                    override fun onError(e: Throwable) {
                        exibeErro(e.message!!)
                    }

                })
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }

}
