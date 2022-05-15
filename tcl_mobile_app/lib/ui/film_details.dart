import 'package:cached_network_image/cached_network_image.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:intl/intl.dart';
import 'package:tcl_mobile_app/ui/buy_ticket_screen.dart';
import 'package:tcl_mobile_app/ui/widgets/error_page.dart';
import 'package:tcl_mobile_app/ui/widgets/home_app_bar.dart';

import '../bloc/films/films_bloc.dart';
import '../constants.dart';
import '../model/Films/single_film_response.dart';
import '../repository/films_repository/films_repository.dart';
import '../repository/films_repository/films_repository_impl.dart';
import '../repository/preferences_utils.dart';

class FilmDetails extends StatefulWidget {
  const FilmDetails({Key? key, required this.filmUuid}) : super(key: key);

  final String filmUuid;

  @override
  State<FilmDetails> createState() => _FilmDetailsState();
}

class _FilmDetailsState extends State<FilmDetails> {
  late FilmRepository filmRepository;
  String? token = "none";

  @override
  void initState() {
    super.initState();
    PreferenceUtils.init();
    filmRepository = FilmRepositoryImpl();

    token = PreferenceUtils.getString("token");
  }

  @override
  Widget build(BuildContext context) {
    return BlocProvider(
      create: (context) {
        return FilmsBloc(filmRepository)..add(GetFilmDetails(widget.filmUuid));
      },
      child: Scaffold(
        body: _createSeeFilm(context, widget.filmUuid),
        appBar: const HomeAppBar(),
      ),
    );
  }
}

Widget _createSeeFilm(BuildContext context, String uuid) {
  return BlocBuilder<FilmsBloc, FilmsState>(
    builder: (context, state) {
      if (state is FilmsInitial) {
        return const Center(child: CircularProgressIndicator());
      } else if (state is FilmErrorState) {
        return ErrorPage(
          message: state.message,
          retry: () {
            context.watch<FilmsBloc>().add(GetFilmDetails(uuid));
          },
        );
      } else if (state is FilmSuccessFetched) {
        return _createPublicView(context, state.film);
      } else {
        return const Text('Not support');
      }
    },
  );
}

Widget _createPublicView(BuildContext context, FilmResponse film) {
  final contentWidth = MediaQuery.of(context).size.width - 160;
  final contentHeight = MediaQuery.of(context).size.height;
  PreferenceUtils.init();
  String? token = PreferenceUtils.getString("token");
  String imageUrl =
      film.poster.replaceAll("http://localhost:8080", Constants.baseUrl);
  return Scaffold(
    body: Container(
      height: contentHeight,
      color: const Color(0xFF263238),
      child: SingleChildScrollView(
        child: Column(children: <Widget>[
          Container(
            height: contentHeight * 0.09,
            color: const Color(0xFF1d1d1d),
            child: Row(
              children: [
                Padding(
                  padding: const EdgeInsets.only(left: 25.0, top: 5.0),
                  child: GestureDetector(
                    onTap: () {
                      Navigator.pop(context);
                    },
                    child: const Icon(Icons.arrow_back, color: Colors.white),
                  ),
                ),
                const Padding(
                  padding: EdgeInsets.only(top: 7.0, left: 20),
                  child: Text(
                    "Detalles de la película",
                    style: TextStyle(color: Colors.white, fontSize: 15.0),
                  ),
                ),
              ],
            ),
          ),
          Row(
            children: [
              Padding(
                padding: const EdgeInsets.only(top: 15.0, left: 25.0),
                child: ClipRRect(
                  borderRadius: BorderRadius.circular(25),
                  child: CachedNetworkImage(
                    placeholder: (context, url) => const Center(
                      child: CircularProgressIndicator(),
                    ),
                    imageUrl: imageUrl,
                    httpHeaders: {"Authorization": "Bearer " + token!},
                    width: contentWidth,
                    height: 300,
                    fit: BoxFit.cover,
                  ),
                ),
              ),
              Column(children: [
                Container(
                  margin: const EdgeInsets.only(left: 25.0),
                  decoration: BoxDecoration(
                    border: Border.all(color: Colors.white, width: 1.5),
                    borderRadius: BorderRadius.circular(15),
                  ),
                  height: 80,
                  width: 80,
                  child: Column(
                    children: [
                      const Padding(
                        padding: EdgeInsets.symmetric(vertical: 4.0),
                        child:
                            Icon(Icons.category, color: Colors.white, size: 35),
                      ),
                      const Text("Género:",
                          style: TextStyle(color: Colors.white, fontSize: 10)),
                      Text(
                        film.genre,
                        style:
                            const TextStyle(color: Colors.white, fontSize: 10),
                      )
                    ],
                  ),
                ),
                Container(
                  margin: const EdgeInsets.only(left: 25.0, top: 15),
                  decoration: BoxDecoration(
                    border: Border.all(color: Colors.white, width: 1.5),
                    borderRadius: BorderRadius.circular(15),
                  ),
                  height: 80,
                  width: 80,
                  child: Column(
                    children: [
                      const Padding(
                        padding: const EdgeInsets.symmetric(vertical: 4.0),
                        child: Icon(Icons.access_time_filled,
                            color: Colors.white, size: 35),
                      ),
                      const Text("Duración:",
                          style: TextStyle(color: Colors.white, fontSize: 10)),
                      Text(
                        film.duration,
                        style:
                            const TextStyle(color: Colors.white, fontSize: 10),
                      )
                    ],
                  ),
                ),
                Container(
                  margin: const EdgeInsets.only(left: 25.0, top: 15),
                  decoration: BoxDecoration(
                    border: Border.all(color: Colors.white, width: 1.5),
                    borderRadius: BorderRadius.circular(15),
                  ),
                  height: 80,
                  width: 80,
                  child: Column(
                    children: [
                      const Padding(
                        padding: const EdgeInsets.symmetric(vertical: 4.0),
                        child: Icon(Icons.calendar_month,
                            color: Colors.white, size: 35),
                      ),
                      const Text("Estreno:",
                          style: TextStyle(color: Colors.white, fontSize: 10)),
                      Text(
                        DateFormat('dd/MM/yyyy')
                            .format(DateTime.parse(film.relaseDate))
                            .toString()
                            .substring(0, 10),
                        style:
                            const TextStyle(color: Colors.white, fontSize: 10),
                      )
                    ],
                  ),
                )
              ])
            ],
          ),
          Padding(
            padding: const EdgeInsets.only(top: 10.0, left: 30.0, right: 30.0),
            child: Align(
                alignment: Alignment.centerLeft,
                child: Text(film.title,
                    style: const TextStyle(
                        color: Colors.white,
                        fontSize: 25,
                        fontWeight: FontWeight.w500))),
          ),
          const Padding(
            padding: EdgeInsets.symmetric(horizontal: 20.0),
            child: Divider(
              height: 20,
              thickness: 1,
              endIndent: 0,
              color: Colors.white,
            ),
          ),
          const Padding(
            padding: EdgeInsets.only(top: 0.0, left: 30.0, right: 30.0),
            child: Align(
                alignment: Alignment.centerLeft,
                child: Text("Descripción",
                    style: TextStyle(
                        color: Colors.white,
                        fontSize: 25,
                        fontWeight: FontWeight.w600))),
          ),
          Padding(
            padding: const EdgeInsets.only(top: 20.0, left: 30.0, right: 30.0),
            child: Text(film.description,
                style: const TextStyle(
                    color: Colors.white,
                    fontSize: 12,
                    fontWeight: FontWeight.w500)),
          ),
          Padding(
            padding: const EdgeInsets.all(40.0),
            child: TextButton(
              onPressed: () => Navigator.push(
                context,
                MaterialPageRoute(
                  builder: (context) => BuyTicketScreen(filmUuid: film.uuid),
                ),
              ),
              style: TextButton.styleFrom(backgroundColor: Colors.white),
              child: const Text(
                'Comprar ticket',
                style: TextStyle(
                  fontSize: 14,
                  color: Color(0xFF263238),
                ),
              ),
            ),
          )
        ]),
      ),
    ),
  );
}
