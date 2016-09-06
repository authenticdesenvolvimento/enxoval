package br.com.mvc;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by robinson on 23/08/2016.
 */
public class Marcadores {
    @SerializedName("id_marcador")
    @Expose
    public String idMarcador;
    @SerializedName("nome_marcador")
    @Expose
    public String nomeMarcador;
    @SerializedName("endereco_marcador")
    @Expose
    public String enderecoMarcador;
    @SerializedName("telefone_marcador")
    @Expose
    public String telefoneMarcador;
    @SerializedName("ddd_marcador")
    @Expose
    public String dddMarcador;
    @SerializedName("url_marcador")
    @Expose
    public String urlMarcador;
    @SerializedName("imagem_url_marcador")
    @Expose
    public String imagemUrlMarcador;
    @SerializedName("promocao_marcador")
    @Expose
    public String promocaoMarcador;
    @SerializedName("latitude_marcador")
    @Expose
    public String latitudeMarcador;
    @SerializedName("longitude_marcador")
    @Expose
    public String longitudeMarcador;

}
