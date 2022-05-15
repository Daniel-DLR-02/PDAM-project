import 'package:cached_network_image/cached_network_image.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:intl/intl.dart';
import 'package:tcl_mobile_app/bloc/films/films_bloc.dart';
import 'package:tcl_mobile_app/constants.dart';
import 'package:tcl_mobile_app/model/Films/single_film_response.dart';
import 'package:tcl_mobile_app/repository/films_repository/films_repository.dart';
import 'package:tcl_mobile_app/repository/films_repository/films_repository_impl.dart';
import 'package:tcl_mobile_app/repository/preferences_utils.dart';
import 'package:tcl_mobile_app/ui/widgets/error_page.dart';

import 'widgets/home_app_bar.dart';

class BuyTicketScreen extends StatefulWidget {
  const BuyTicketScreen({ Key? key, required this.filmUuid }) : super(key: key);
  final String filmUuid;

  @override
  State<BuyTicketScreen> createState() => _BuyTicketScreenState();
}

class _BuyTicketScreenState extends State<BuyTicketScreen> {
  late FilmRepository filmRepository;
  String? token = "none";
  String sessionId = "ac106372-80c7-15d7-8180-c737802e0002";

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
  final contentWidth = MediaQuery.of(context).size.width - 30;
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
            height: 65,
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
                    "Selección de sitios",
                    style: TextStyle(color: Colors.white, fontSize: 15.0),
                  ),
                ),
              ],
            ),
          ),
          Row(
            children: [
              Padding(
                padding: const EdgeInsets.symmetric(horizontal:30.0,vertical: 8),
                child: Container(
                  width: contentWidth-30,
                  height: contentHeight * 0.4,
                  decoration: const BoxDecoration(
                    color: Colors.black
                  ),
                  child: Column(
                    children: [
                      Padding(
                        padding: const EdgeInsets.only(bottom:30.0),
                        child: Container(
                          width: contentWidth,
                          height: 10,
                          decoration: const BoxDecoration(
                            color: Color(0xFFeceff1)
                          ),
                        ),
                      ),
                      Row(
                        children: [
                          Container(
                            width:15,
                            height: 15,
                            decoration: const BoxDecoration(
                              color: Colors.white
                            ),
                          ),
                        ],
                      )
                    ],
                  ),
                ),
              ),
            ],
          ),
          
        ]),
      ),
    ),
  );
}
