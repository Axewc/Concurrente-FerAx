{-  Práctica 1: Introducción a Haskell
    
    Equipo:
        Carmen Paola Innes Barrón
        Melissa Vázquez González
        Fernanda Garduño Ballesteros
        Jean Duran Villanueva
        Daniel Beristáin Hernández
        -}

module Practica1 where

    import Data.List (sort)

    data BST a = Empty | Node a ( BST a ) ( BST a ) deriving Show




    {- 1. Define la función groupAnagrams tal que recibe una lista de String y 
    devuelve una lista con los anagramas agrupados. Un anagrama es una palabra 
    o frase formada al reorganizar las letras de otra palabra o frase, utilizando
    todas las letras originales exactamente una vez.-}


    -- función auxiliar para revisar si dos palabras son anagramas
    areAnagrams :: String -> String -> Bool
    areAnagrams x y
        | sort x == sort y = True -- sort ordena los caracteres en orden alfabetico para compararlos
        | length x /= length y = False -- si las palabras no tienen la misma longitud no pueden ser anagramas
        | otherwise = False -- si no se cumple ninguna de las condiciones anteriores no son anagramas

    -- función auxiliar 



    groupAnagrams :: [String] -> [[String]]
    groupAnagrams [] = [] -- si la lista está vacía no hay anagramas
    groupAnagrams (x:xs) = (x:listAnagram x xs) : groupAnagrams (listNonAnagram x xs) -- se agrega la palabra a la lista de anagramas y llamamos recursivamente a la función con la lista de palabras que no son anagramas


    {- 2. Define la función subsets tal que recibe una lista de elementos únicos y 
    devuelve el conjunto potencia.-}
    subsets :: [a] -> [[a]]
    subsets [] = [[]]
    subsets (x:xs) = [x:ys|ys <- subsets xs]++subsets xs


    {- 3. El elemento mayoritario es el elemento que aparece más de ⌊n/2⌋ veces, 
    donde n es la longitud de la lista. Define la función majorityElem tal que 
    recibe una lista y devuelve su elemento mayoritario.
    La solución debe ser de complejidad O(n) en tiempo y O(1) en el espacio. -}
    majorityElemAux :: Eq a => a -> Int -> [a] -> a
    majorityElemAux candidato apariciones [] = candidato
    majorityElemAux candidato apariciones (x: xs)
        | candidato == x = majorityElemAux candidato (apariciones + 1) xs
        | apariciones == 1 = majorityElemAux x 1 xs
        | otherwise =  majorityElemAux candidato (apariciones - 1) xs

    majorityElem :: Eq a => [ a ] -> a
    majorityElem (x: xs) = majorityElemAux x 1 xs


    {- 4. Define la función coins tal que recibe una lista de monedas de diferentes 
    denominaciones y una cantidad total de dinero, y devuelve si es posible 
    completar la cantidad usando únicamente ese tipo de monedas. -}
    coins :: [Int] -> Int -> Bool
    coins [] dinero = False -- Sin alguna moneda no se puede dar una cantidad
    coins lista_monedas 0 = True -- Si la cantidad es 0 se pueden llegar con 0 monedas a esa cantidad
    coins (h:t) x
        | x < 0     = False -- No se pudo completar con las monedas dadas
        | otherwise = coins (h:t) (x - h) || coins t x 
        {- Se le resta a la cantidad la cabeza de la lista y se hace una llamada con la nueva 
        cantidad y las distintas monedas para ver si se sigue completando la cantidad.
        O bien se "explora" el segundo camino y se llama recursivamente a la función con la cola 
        de la lista y la cantidad sin restar. -}


    {- 5. Define la función isBST tal que recibe un árbol binario y devuelve si 
    es un árbol de búsqueda binario válido. Un BST válido se define de la 
    siguiente manera:
    (a) El subárbol izquierdo contiene solo valores menores que la raíz.
    (b) El subárbol derecho contiene solo valores mayores que la raíz.
    (c) Ambos subárboles deben ser árboles de búsqueda binarios. -}
    isBSTaux :: Ord a => [a] -> Bool
    isBSTaux( x:[]) = True
    isBSTaux (x:y:xs)
        | x < y = isBSTaux (y:xs)
        |otherwise = False

    bstToList:: BST a -> [a]
    bstToList Empty = []
    bstToList (Node a izq der) = bstToList izq ++ [a] ++ bstToList der

    isBST :: BST Int -> Bool
    isBST (Node a izq der) = isBSTaux (bstToList (Node a izq der))


    {- 6. Define la función kthElem tal que recibe un árbol de búsqueda binaria 
    y un número entero k, y devuelve el k-ésimo valor más pequeño. -}
    kthElem :: BST a -> Int -> a
    kthElem t k = (bstToList t) !! (k - 1)


    -- como correr el programa
    -- ghci Practica1.hs -o Practica1 -main-is Practica1.main -outputdir build -O2 