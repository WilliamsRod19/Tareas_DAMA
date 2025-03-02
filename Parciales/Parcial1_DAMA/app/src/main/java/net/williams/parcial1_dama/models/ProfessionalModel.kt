package net.williams.parcial1_dama.models
import java.io.Serializable

data class ProfessionalModel (
    val pName: String,
    val pProfession: String,
    val pExperience: String,
    val pAbout: String,
    val pImage: Int,
    val pPhone: String
) : Serializable